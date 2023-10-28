package com.javajober.spaceWall.service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.javajober.core.exception.ApiStatus;
import com.javajober.core.exception.ApplicationException;
import com.javajober.space.repository.AddSpaceRepository;

import com.javajober.space.domain.AddSpace;
import com.javajober.member.domain.Member;
import com.javajober.member.repository.MemberRepository;
import com.javajober.spaceWall.dto.request.*;
import com.javajober.spaceWall.dto.response.SpaceWallSaveResponse;
import com.javajober.spaceWall.filedto.SpaceWallSaveRequest;
import com.javajober.spaceWall.domain.BlockType;
import com.javajober.spaceWall.domain.FlagType;
import com.javajober.spaceWall.domain.SpaceWall;
import com.javajober.spaceWall.spaceWallCategory.domain.SpaceWallCategoryType;
import com.javajober.spaceWall.repository.SpaceWallRepository;
import com.javajober.spaceWall.strategy.BlockJsonProcessor;
import com.javajober.spaceWall.strategy.BlockStrategyFactory;
import com.javajober.spaceWall.strategy.FixBlockStrategy;
import com.javajober.spaceWall.strategy.MoveBlockStrategy;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class SpaceWallService {

	private static final Long INITIAL_POSITION = 1L;
	private final SpaceWallRepository spaceWallRepository;
	private final MemberRepository memberRepository;
	private final AddSpaceRepository addSpaceRepository;
	private final BlockStrategyFactory blockStrategyFactory;
	private final BlockJsonProcessor blockJsonProcessor;

	public SpaceWallService(final SpaceWallRepository spaceWallRepository,
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
	public SpaceWallSaveResponse save(final Long memberId, final SpaceWallStringRequest spaceWallRequest, final FlagType flagType) {

		Member member = memberRepository.findMember(memberId);

		DataStringSaveRequest data = spaceWallRequest.getData();

		AddSpace addSpace = addSpaceRepository.findAddSpace(data.getSpaceId());

		validateSpaceOwnership(member, addSpace);

		validateAddSpaceId(addSpace.getId(), flagType);

		checkDuplicateShareURL(data.getShareURL(), flagType);

		SpaceWallCategoryType spaceWallCategoryType = SpaceWallCategoryType.findSpaceWallCategoryTypeByString(data.getCategory());

		ArrayNode blockInfoArray = blockJsonProcessor.createArrayNode();

		AtomicLong blocksPositionCounter = new AtomicLong(INITIAL_POSITION);

		processWallInfoBlock(data, blockInfoArray, blocksPositionCounter);

		List<BlockSaveRequest<?>> blocks = data.getBlocks();

		processBlocks(blocks, blockInfoArray, blocksPositionCounter);

		processStyleSettingBlock(data, blockInfoArray, blocksPositionCounter);

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

	private void processWallInfoBlock(final DataStringSaveRequest data, final ArrayNode blockInfoArray, final AtomicLong blocksPositionCounter) {
		String wallInfoBlockStrategyName = BlockType.WALL_INFO_BLOCK.getStrategyName();
		FixBlockStrategy wallInfoBlockStrategy = blockStrategyFactory.findFixBlockStrategy(wallInfoBlockStrategyName);

		Long wallInfoBlockPosition = blocksPositionCounter.getAndIncrement();
		wallInfoBlockStrategy.saveStringBlocks(data, blockInfoArray, wallInfoBlockPosition);
	}

	private void processBlocks(final List<BlockSaveRequest<?>> blocks, final ArrayNode blockInfoArray, final AtomicLong blocksPositionCounter) {
		blocks.forEach(block -> {

			BlockType blockType = BlockType.findBlockTypeByString(block.getBlockType());
			Long position = blocksPositionCounter.getAndIncrement();

			String strategyName = blockType.getStrategyName();
			MoveBlockStrategy moveBlockStrategy = blockStrategyFactory.findMoveBlockStrategy(strategyName);

			moveBlockStrategy.saveStringBlocks(block, blockInfoArray, position);
		});
	}

	private void processStyleSettingBlock(final DataStringSaveRequest data, final ArrayNode blockInfoArray, final AtomicLong blocksPositionCounter) {

		String styleSettingBlockStrategyName = BlockType.STYLE_SETTING.getStrategyName();
		FixBlockStrategy styleSettingBlockStrategy = blockStrategyFactory.findFixBlockStrategy(styleSettingBlockStrategyName);

		Long styleSettingPosition = blocksPositionCounter.getAndIncrement();
		styleSettingBlockStrategy.saveStringBlocks(data, blockInfoArray, styleSettingPosition);
	}

	private Long saveSpaceWall(final SpaceWallCategoryType spaceWallCategoryType, final Member member, final AddSpace addSpace, final String shareURL, final FlagType flagType, final ArrayNode blockInfoArray) {

		String blockInfoArrayAsString = blockInfoArray.toString();
		SpaceWall spaceWall = SpaceWallSaveRequest.toEntity(spaceWallCategoryType, member, addSpace, shareURL, flagType, blockInfoArrayAsString);

		return spaceWallRepository.save(spaceWall).getId();
	}

	@Transactional
	public void updateIsPublic(IsPublicUpdateRequest publicUpdateRequest, Long memberId) {
		Long spaceId = publicUpdateRequest.getSpaceId();
		Long spaceWallId = publicUpdateRequest.getSpaceWallId();
		SpaceWall spaceWall = spaceWallRepository.findSpaceWall(spaceWallId, spaceId, memberId, FlagType.SAVED);

		Boolean isPublic = publicUpdateRequest.getIsPublic();
		spaceWall.updateIsPublic(isPublic);
		spaceWallRepository.save(spaceWall);
	}

	@Transactional
	public SpaceWallSaveResponse update(final Long memberId, final SpaceWallStringUpdateRequest spaceWallUpdateRequest, final FlagType flagType) {

		DataStringUpdateRequest data = spaceWallUpdateRequest.getData();

		Long spaceWallId = data.getSpaceWallId();

		Member member = memberRepository.findMember(memberId);

		AddSpace addSpace = addSpaceRepository.findAddSpace(data.getSpaceId());

		validateSpaceOwnership(member, addSpace);

		SpaceWall spaceWall = spaceWallRepository.findSpaceWall(spaceWallId, addSpace.getId(), memberId, flagType);

		ArrayNode blockInfoArray = blockJsonProcessor.createArrayNode();

		List<BlockSaveRequest<?>> blocksRequest = data.getBlocks();

		AtomicLong blocksPositionCounter = new AtomicLong(INITIAL_POSITION);

		updateWallInfoBlock(data, blockInfoArray, blocksPositionCounter);

		Map<BlockType, Set<Long>> existingBlockIdsByType = new HashMap<>();

		Map<BlockType, Set<Long>> updatedBlockIdsByType = new HashMap<>();

		String existingBlocks = spaceWall.getBlocks();

		processBlocks(blocksRequest, blockInfoArray, blocksPositionCounter, existingBlockIdsByType, updatedBlockIdsByType, existingBlocks);

		deleteRemainingBlocks(existingBlockIdsByType, updatedBlockIdsByType);

		updateStyleSettingBlock(data, blockInfoArray, blocksPositionCounter);

		String blocks = blockInfoArray.toString();

		spaceWall.update(data, flagType, blocks);

		spaceWallId = spaceWallRepository.save(spaceWall).getId();

		return new SpaceWallSaveResponse(spaceWallId);
	}

	private void updateWallInfoBlock(final DataStringUpdateRequest data, final ArrayNode blockInfoArray, final AtomicLong blocksPositionCounter) {
		String wallInfoBlockStrategyName = BlockType.WALL_INFO_BLOCK.getStrategyName();
		FixBlockStrategy wallInfoBlockStrategy = blockStrategyFactory.findFixBlockStrategy(wallInfoBlockStrategyName);

		Long wallInfoBlockPosition = blocksPositionCounter.getAndIncrement();
		wallInfoBlockStrategy.updateStringBlocks(data, blockInfoArray, wallInfoBlockPosition);
	}

	private void processBlocks(final List<BlockSaveRequest<?>> blocksRequest, final ArrayNode blockInfoArray, final AtomicLong blocksPositionCounter,
		                       final Map<BlockType, Set<Long>> existingBlockIdsByType, final Map<BlockType, Set<Long>> updatedBlockIdsByType, final String existingBlocks) {

		blocksRequest.forEach(block -> {
			BlockType blockType = BlockType.findBlockTypeByString(block.getBlockType());
			Long position = blocksPositionCounter.getAndIncrement();
			String strategyName = blockType.getStrategyName();
			MoveBlockStrategy moveBlockStrategy = blockStrategyFactory.findMoveBlockStrategy(strategyName);
			processBlock(block, blockInfoArray, position, existingBlockIdsByType, updatedBlockIdsByType, existingBlocks, blockType, moveBlockStrategy);
		});
	}

	private void processBlock(final BlockSaveRequest<?> block, final ArrayNode blockInfoArray, final Long position,
		final Map<BlockType, Set<Long>> existingBlockIdsByType, final Map<BlockType, Set<Long>> updatedBlockIdsByType, final String existingBlocks, final BlockType blockType, MoveBlockStrategy moveBlockStrategy) {

		if (!existingBlockIdsByType.containsKey(blockType)) {
			Set<Long> existingBlockIdsForThisType = blockJsonProcessor.existingBlockIds(existingBlocks, blockType);
			existingBlockIdsByType.put(blockType, existingBlockIdsForThisType);
		}

		Set<Long> updatedIds = moveBlockStrategy.updateStringBlocks(block, blockInfoArray, position);

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
				MoveBlockStrategy moveBlockStrategy = blockStrategyFactory.findMoveBlockStrategy(
					(blockType.getStrategyName()));
				moveBlockStrategy.deleteAllById(remainingBlockIds);
			}
		}
	}

	private void updateStyleSettingBlock(final DataStringUpdateRequest data, final ArrayNode blockInfoArray, final AtomicLong blocksPositionCounter) {

		String styleSettingBlockStrategyName = BlockType.STYLE_SETTING.getStrategyName();
		FixBlockStrategy styleSettingBlockStrategy = blockStrategyFactory.findFixBlockStrategy(styleSettingBlockStrategyName);

		Long styleSettingPosition = blocksPositionCounter.getAndIncrement();
		styleSettingBlockStrategy.updateStringBlocks(data, blockInfoArray, styleSettingPosition);
	}
}