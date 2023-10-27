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

import java.util.List;
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

        validateAddSpaceId(addSpace.getId());

        checkDuplicateShareURL(data.getShareURL());

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

    private void validateAddSpaceId (final Long spaceId) {
        boolean existsSpaceId = spaceWallRepository.existsByAddSpaceId(spaceId);
        if (existsSpaceId) {
            throw new ApplicationException(ApiStatus.INVALID_DATA, "스페이스 하나당 공유페이지 하나만 생성 가능합니다.");
        }
    }

    private void checkDuplicateShareURL(final String shareURL) {
        boolean existsShareURL = spaceWallRepository.existsByShareURLAndFlag(shareURL, FlagType.SAVED);
        if (existsShareURL) {
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
                MoveBlockStrategy blockProcessingStrategy = blockStrategyFactory.findMoveBlockStrategy(strategyName);

                if (BlockStrategyName.FileBlockStrategy.name().equals(blockProcessingStrategy.getStrategyName())) {
                    blockProcessingStrategy.uploadFile(files.get(fileIndexCounter.getAndIncrement()));
                }

                blockProcessingStrategy.saveBlocks(block, blockInfoArray, position);
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


        return new SpaceWallSaveResponse(1L);
    }
}