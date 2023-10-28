package com.javajober.spaceWall.service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.javajober.core.exception.ApiStatus;
import com.javajober.core.exception.ApplicationException;
import com.javajober.member.domain.Member;
import com.javajober.member.repository.MemberRepository;
import com.javajober.space.domain.AddSpace;
import com.javajober.space.repository.AddSpaceRepository;
import com.javajober.spaceWall.domain.BlockType;
import com.javajober.spaceWall.domain.FlagType;
import com.javajober.spaceWall.domain.SpaceWall;
import com.javajober.spaceWall.dto.request.BlockSaveRequest;
import com.javajober.spaceWall.filedto.DataSaveRequest;
import com.javajober.spaceWall.filedto.DataUpdateRequest;
import com.javajober.spaceWall.filedto.SpaceWallSaveRequest;
import com.javajober.spaceWall.dto.response.SpaceWallSaveResponse;
import com.javajober.spaceWall.filedto.SpaceWallUpdateRequest;
import com.javajober.spaceWall.repository.SpaceWallRepository;
import com.javajober.spaceWall.spaceWallCategory.domain.SpaceWallCategoryType;

import com.javajober.spaceWall.strategy.BlockJsonProcessor;
import com.javajober.spaceWall.strategy.BlockStrategyFactory;
import com.javajober.spaceWall.strategy.BlockStrategyName;
import com.javajober.spaceWall.strategy.FixBlockStrategy;
import com.javajober.spaceWall.strategy.MoveBlockStrategy;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class FileUploadService {
    private static final Long INITIAL_POSITION = 1L;
    private final SpaceWallRepository spaceWallRepository;
    private final MemberRepository memberRepository;
    private final AddSpaceRepository addSpaceRepository;
    private final BlockStrategyFactory blockStrategyFactory;
    private final BlockJsonProcessor blockJsonProcessor;

    public FileUploadService(final SpaceWallRepository spaceWallRepository,
        final MemberRepository memberRepository,
        final AddSpaceRepository addSpaceRepository,
        final BlockStrategyFactory blockStrategyFactory,
        final BlockJsonProcessor blockJsonProcessor) {

        this.spaceWallRepository = spaceWallRepository;
        this.memberRepository = memberRepository;
        this.addSpaceRepository = addSpaceRepository;
        this.blockStrategyFactory = blockStrategyFactory;
        this.blockJsonProcessor = blockJsonProcessor;
    }

    @Transactional
    public SpaceWallSaveResponse save(final Long memberId, final SpaceWallSaveRequest spaceWallRequest, FlagType flagType,
                                      final List<MultipartFile> files, final MultipartFile backgroundImgURL,
                                      final MultipartFile wallInfoImgURL, final MultipartFile styleImgURL) {

        Member member = memberRepository.findMember(memberId);

        DataSaveRequest data = spaceWallRequest.getData();

        AddSpace addSpace = addSpaceRepository.findAddSpace(data.getSpaceId());

        validateSpaceOwnership(member, addSpace);

        validateAddSpaceId(addSpace.getId(), flagType);

        checkDuplicateShareURL(data.getShareURL(), flagType);

        SpaceWallCategoryType spaceWallCategoryType = SpaceWallCategoryType.findSpaceWallCategoryTypeByString(data.getCategory());

        ArrayNode blockInfoArray = blockJsonProcessor.createArrayNode();

        AtomicLong blocksPositionCounter = new AtomicLong(INITIAL_POSITION);

        processWallInfoBlock(backgroundImgURL, wallInfoImgURL, data, blockInfoArray, blocksPositionCounter);

        List<BlockSaveRequest<?>> blocks = data.getBlocks();

        processBlocks(blocks, blockInfoArray, blocksPositionCounter, files);

        processStyleSettingBlock(styleImgURL, data, blockInfoArray, blocksPositionCounter);

        String shareURL = spaceWallRequest.getData().getShareURL();

        Long spaceWallId = saveSpaceWall(spaceWallCategoryType, member, addSpace, shareURL, flagType, blockInfoArray);

        return new SpaceWallSaveResponse(spaceWallId);
    }

    private void validateSpaceOwnership(final Member member, final AddSpace addSpace) {
        Long memberId = member.getId();
        Long spaceId = addSpace.getMember().getId();

        if (!memberId.equals(spaceId)) {
            throw new ApplicationException(ApiStatus.INVALID_DATA, "사용자의 스페이스를 찾을 수가 없습니다.");
        }
    }

    private void validateAddSpaceId (final Long spaceId, FlagType flagType) {
        boolean existsSpaceId = spaceWallRepository.existsByAddSpaceId(spaceId);
        if (existsSpaceId && flagType == FlagType.SAVED) {
            throw new ApplicationException(ApiStatus.INVALID_DATA, "스페이스 하나당 공유페이지 하나만 생성 가능합니다.");
        }
    }

    private void checkDuplicateShareURL(final String shareURL, FlagType flagType) {
        boolean existsShareURL = spaceWallRepository.existsByShareURLAndFlag(shareURL, FlagType.SAVED);
        if (existsShareURL && flagType == FlagType.SAVED) {
            throw new ApplicationException(ApiStatus.ALREADY_EXIST, "이미 사용중인 shareURL입니다.");
        }
    }

    private void processWallInfoBlock(final MultipartFile backgroundImgURL, final MultipartFile wallInfoImgURL,
        final DataSaveRequest data, final ArrayNode blockInfoArray,
        final AtomicLong blocksPositionCounter) {

        String wallInfoBlockStrategyName = BlockType.WALL_INFO_BLOCK.getStrategyName();
        FixBlockStrategy wallInfoBlockStrategy = blockStrategyFactory.findFixBlockStrategy(wallInfoBlockStrategyName);

        wallInfoBlockStrategy.uploadTwoFiles(backgroundImgURL, wallInfoImgURL);

        Long wallInfoBlockPosition = blocksPositionCounter.getAndIncrement();
        wallInfoBlockStrategy.saveBlocks(data, blockInfoArray, wallInfoBlockPosition);
    }

    private void processBlocks(final List<BlockSaveRequest<?>> blocks, final ArrayNode blockInfoArray,
        final AtomicLong blocksPositionCounter, final List<MultipartFile> files) {

        try {
            AtomicInteger fileIndexCounter = new AtomicInteger();

            blocks.forEach(block -> {

                BlockType blockType = BlockType.findBlockTypeByString(block.getBlockType());
                Long position = blocksPositionCounter.getAndIncrement();

                String strategyName = blockType.getStrategyName();
                MoveBlockStrategy moveBlockStrategy = blockStrategyFactory.findMoveBlockStrategy(strategyName);

                if (BlockStrategyName.FileBlockStrategy.name().equals(moveBlockStrategy.getStrategyName())) {
                    moveBlockStrategy.uploadFile(files.get(fileIndexCounter.getAndIncrement()));
                }

                moveBlockStrategy.saveBlocks(block, blockInfoArray, position);
            });
        } catch (IndexOutOfBoundsException e) {
            throw new ApplicationException(ApiStatus.INVALID_DATA, "파일이 첨부되지 않은 파일블록이 있습니다.");
        }
    }

    private void processStyleSettingBlock(final MultipartFile styleImgURL, final DataSaveRequest data, final ArrayNode blockInfoArray,
        final AtomicLong blocksPositionCounter) {

        String styleSettingBlockStrategyName = BlockType.STYLE_SETTING.getStrategyName();
        FixBlockStrategy styleSettingBlockStrategy = blockStrategyFactory.findFixBlockStrategy(styleSettingBlockStrategyName);

        styleSettingBlockStrategy.uploadSingleFile(styleImgURL);

        Long styleSettingPosition = blocksPositionCounter.getAndIncrement();
        styleSettingBlockStrategy.saveBlocks(data, blockInfoArray, styleSettingPosition);
    }
    private Long saveSpaceWall(final SpaceWallCategoryType spaceWallCategoryType, final Member member, final AddSpace addSpace, final String shareURL, final FlagType flagType, final ArrayNode blockInfoArray) {

        String blockInfoArrayAsString = blockInfoArray.toString();
        SpaceWall spaceWall = SpaceWallSaveRequest.toEntity(spaceWallCategoryType, member, addSpace, shareURL, flagType, blockInfoArrayAsString);

        return spaceWallRepository.save(spaceWall).getId();
    }

    @Transactional
    public SpaceWallSaveResponse update(final Long memberId, final SpaceWallUpdateRequest spaceWallRequest, FlagType flagType,
                                        final List<MultipartFile> files, final MultipartFile backgroundImgURL,
                                        final MultipartFile wallInfoImgURL, final MultipartFile styleImgURL){

        DataUpdateRequest data = spaceWallRequest.getData();

        Long spaceWallId = data.getSpaceWallId();

        Member member = memberRepository.findMember(memberId);

        AddSpace addSpace = addSpaceRepository.findAddSpace(data.getSpaceId());

        validateSpaceOwnership(member, addSpace);

        SpaceWall spaceWall = spaceWallRepository.findSpaceWall(spaceWallId, addSpace.getId(), memberId, flagType);

        ArrayNode blockInfoArray = blockJsonProcessor.createArrayNode();

        List<BlockSaveRequest<?>> blocksRequest = data.getBlocks();

        AtomicLong blocksPositionCounter = new AtomicLong(INITIAL_POSITION);

        updateWallInfoBlock(data, backgroundImgURL, wallInfoImgURL, blockInfoArray, blocksPositionCounter);

        Map<BlockType, Set<Long>> existingBlockIdsByType = new HashMap<>();

        Map<BlockType, Set<Long>> updatedBlockIdsByType = new HashMap<>();

        String existingBlocks = spaceWall.getBlocks();

        processBlocks(blocksRequest, files, blockInfoArray, blocksPositionCounter, existingBlockIdsByType, updatedBlockIdsByType, existingBlocks);

        deleteRemainingBlocks(existingBlockIdsByType, updatedBlockIdsByType);

        updateStyleSettingBlock(data, styleImgURL, blockInfoArray, blocksPositionCounter);

        String blocks = blockInfoArray.toString();

        spaceWall.fileUpdate(data, flagType, blocks);

        spaceWallId = spaceWallRepository.save(spaceWall).getId();

        return new SpaceWallSaveResponse(spaceWallId);
    }

    private void updateWallInfoBlock(final DataUpdateRequest data, final MultipartFile backgroundImgURL, final MultipartFile wallInfoImgURL,
        final ArrayNode blockInfoArray, final AtomicLong blocksPositionCounter) {

        String wallInfoBlockStrategyName = BlockType.WALL_INFO_BLOCK.getStrategyName();

        FixBlockStrategy wallInfoBlockStrategy = blockStrategyFactory.findFixBlockStrategy(wallInfoBlockStrategyName);

        wallInfoBlockStrategy.uploadTwoFiles(backgroundImgURL, wallInfoImgURL);

        Long wallInfoBlockPosition = blocksPositionCounter.getAndIncrement();

        wallInfoBlockStrategy.updateBlocks(data, blockInfoArray, wallInfoBlockPosition);
    }

    private void processBlocks(final List<BlockSaveRequest<?>> blocks, final List<MultipartFile> files, final ArrayNode blockInfoArray, final AtomicLong blocksPositionCounter,
        final Map<BlockType, Set<Long>> existingBlockIdsByType, final Map<BlockType, Set<Long>> updatedBlockIdsByType, final String existingBlocks) {

        try {
            AtomicInteger fileIndexCounter = new AtomicInteger();

            blocks.forEach(block -> {
                BlockType blockType = BlockType.findBlockTypeByString(block.getBlockType());

                Long position = blocksPositionCounter.getAndIncrement();
                String strategyName = blockType.getStrategyName();
                MoveBlockStrategy moveBlockStrategy = blockStrategyFactory.findMoveBlockStrategy(strategyName);

                if (BlockStrategyName.FileBlockStrategy.name().equals(moveBlockStrategy.getStrategyName())) {
                    moveBlockStrategy.uploadFile(files.get(fileIndexCounter.getAndIncrement()));
                }

                processBlock(block, blockInfoArray, position, existingBlockIdsByType, updatedBlockIdsByType,
                    existingBlocks, blockType, moveBlockStrategy);
            });
        }  catch (IndexOutOfBoundsException e) {
            throw new ApplicationException(ApiStatus.INVALID_DATA, "파일이 첨부되지 않은 파일블록이 있습니다.");
        }
    }

    private void processBlock(final BlockSaveRequest<?> block, final ArrayNode blockInfoArray, final Long position,
        final Map<BlockType, Set<Long>> existingBlockIdsByType, final Map<BlockType, Set<Long>> updatedBlockIdsByType, final String existingBlocks, final BlockType blockType, MoveBlockStrategy moveBlockStrategy) {

        if (!existingBlockIdsByType.containsKey(blockType)) {
            Set<Long> existingBlockIdsForThisType = blockJsonProcessor.existingBlockIds(existingBlocks, blockType);
            existingBlockIdsByType.put(blockType, existingBlockIdsForThisType);
        }

        Set<Long> updatedIds = moveBlockStrategy.updateBlocks(block, blockInfoArray, position);

        updatedIds.forEach(blockId ->
            blockJsonProcessor.addBlockInfoToArray(blockInfoArray, position, blockType, blockId, block.getBlockUUID()));

        if (!updatedBlockIdsByType.containsKey(blockType)) {
            updatedBlockIdsByType.put(blockType, new HashSet<>());
        }

        updatedBlockIdsByType.get(blockType).addAll(updatedIds);
    }

    private void deleteRemainingBlocks(final Map<BlockType, Set<Long>> existingBlockIdsByType, final Map<BlockType, Set<Long>> updatedBlockIdsByType) {

        for (BlockType blockType : existingBlockIdsByType.keySet()) {
            Set<Long> remainingBlockIds = existingBlockIdsByType.get(blockType);
            remainingBlockIds.removeAll(updatedBlockIdsByType.getOrDefault(blockType, Collections.emptySet()));

            if (!remainingBlockIds.isEmpty()) {
                MoveBlockStrategy moveBlockStrategy = blockStrategyFactory.findMoveBlockStrategy((blockType.getStrategyName()));
                moveBlockStrategy.deleteAllById(remainingBlockIds);
            }
        }
    }

    private void updateStyleSettingBlock(final DataUpdateRequest data,  final MultipartFile styleImgURL, final ArrayNode blockInfoArray, final AtomicLong blocksPositionCounter) {

        String styleSettingBlockStrategyName = BlockType.STYLE_SETTING.getStrategyName();

        FixBlockStrategy styleSettingBlockStrategy = blockStrategyFactory.findFixBlockStrategy(styleSettingBlockStrategyName);

        styleSettingBlockStrategy.uploadSingleFile(styleImgURL);

        Long styleSettingPosition = blocksPositionCounter.getAndIncrement();

        styleSettingBlockStrategy.updateBlocks(data, blockInfoArray, styleSettingPosition);
    }
}