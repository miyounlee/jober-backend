package com.javajober.spaceWall.strategy.impl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.javajober.blocks.snsBlock.dto.request.SNSBlockUpdateRequest;
import com.javajober.blocks.snsBlock.dto.response.SNSBlockResponse;
import com.javajober.core.util.response.CommonResponse;

import org.springframework.stereotype.Component;

import com.javajober.blocks.snsBlock.domain.SNSBlock;
import com.javajober.blocks.snsBlock.dto.request.SNSBlockSaveRequest;
import com.javajober.blocks.snsBlock.repository.SNSBlockRepository;
import com.javajober.spaceWall.domain.BlockType;
import com.javajober.spaceWall.dto.request.BlockSaveRequest;
import com.javajober.spaceWall.strategy.BlockJsonProcessor;
import com.javajober.spaceWall.strategy.BlockStrategyName;
import com.javajober.spaceWall.strategy.MoveBlockStrategy;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SNSBlockStrategy implements MoveBlockStrategy {

	private final BlockJsonProcessor blockJsonProcessor;
	private final SNSBlockRepository snsBlockRepository;

	public SNSBlockStrategy(final BlockJsonProcessor blockJsonProcessor, final SNSBlockRepository snsBlockRepository) {
		this.blockJsonProcessor = blockJsonProcessor;
		this.snsBlockRepository = snsBlockRepository;
	}

	@Override
	public void saveStringBlocks(final BlockSaveRequest<?> block, final ArrayNode blockInfoArray, final Long position) {
		List<SNSBlockSaveRequest> snsBlockRequests = convertSubDataToSNSBlockSaveRequests(block.getSubData());

		List<SNSBlock> snsBlocks = convertToSNSBlocks(snsBlockRequests);

		List<SNSBlock> savedSNSBlocks = saveAllSNSBlock(snsBlocks);

		addToSNSBlockInfoArray(savedSNSBlocks, blockInfoArray, position, block.getBlockUUID());
	}

	@Override
	public void saveBlocks(final BlockSaveRequest<?> block, final ArrayNode blockInfoArray, final Long position) {
		List<SNSBlockSaveRequest> snsBlockRequests = convertSubDataToSNSBlockSaveRequests(block.getSubData());

		List<SNSBlock> snsBlocks = convertToSNSBlocks(snsBlockRequests);

		List<SNSBlock> savedSNSBlocks = saveAllSNSBlock(snsBlocks);

		addToSNSBlockInfoArray(savedSNSBlocks, blockInfoArray, position, block.getBlockUUID());
	}

	private List<SNSBlockSaveRequest> convertSubDataToSNSBlockSaveRequests(final List<?> subData) {
		List<SNSBlockSaveRequest> snsBlockRequests = new ArrayList<>();

		subData.forEach(block -> {
			SNSBlockSaveRequest request = blockJsonProcessor.convertValue(block, SNSBlockSaveRequest.class);
			snsBlockRequests.add(request);
		});
		return snsBlockRequests;
	}

	private List<SNSBlock> convertToSNSBlocks(final List<SNSBlockSaveRequest> snsBlockSaveRequests) {
		return snsBlockSaveRequests.stream()
			.map(SNSBlockSaveRequest::toEntity)
			.collect(Collectors.toList());
	}

	private List<SNSBlock> saveAllSNSBlock(final List<SNSBlock> snsBlocks) {
		return snsBlockRepository.saveAll(snsBlocks);
	}

	private void addToSNSBlockInfoArray (final List<SNSBlock> savedSNSBlocks, final ArrayNode blockInfoArray, final Long position, String snsBlockUUID) {
		savedSNSBlocks.forEach(savedSNSBlock ->
			blockJsonProcessor.addBlockInfoToArray(blockInfoArray, position, BlockType.SNS_BLOCK, savedSNSBlock.getId(), snsBlockUUID)
		);
	}

	@Override
	public List<CommonResponse> createMoveBlockDTO(final List<JsonNode> blocksWithSamePosition) {
		List<CommonResponse> subData = new ArrayList<>();
		for (JsonNode block : blocksWithSamePosition) {
			long blockId = block.path("block_id").asLong();
			SNSBlock snsBlock = snsBlockRepository.findSNSBlock(blockId);
			subData.add(SNSBlockResponse.from(snsBlock));
		}
		return subData;
	}

	@Override
	public Set<Long> updateStringBlocks(final BlockSaveRequest<?> blocks, final ArrayNode blockInfoArray, final Long position) {

		List<SNSBlock> snsBlocks = new ArrayList<>();

		blocks.getSubData().forEach(block -> {
			SNSBlockUpdateRequest request = blockJsonProcessor.convertValue(block, SNSBlockUpdateRequest.class);
			SNSBlock snsBlock = saveOrUpdateSNSBlock(request);
			snsBlocks.add(snsBlock);
		});

		List<SNSBlock> updatedSNSBlocks = snsBlockRepository.saveAll(snsBlocks);

		return updatedSNSBlocks.stream().map(SNSBlock::getId).collect(Collectors.toCollection(LinkedHashSet::new));

	}

	private SNSBlock saveOrUpdateSNSBlock(final SNSBlockUpdateRequest request) {

		if(request.getSnsBlockId() == null) {
			return SNSBlockUpdateRequest.toEntity(request);
		}

		SNSBlock snsBlock = snsBlockRepository.findSNSBlock(request.getSnsBlockId());
		snsBlock.update(request);

		return snsBlock;
	}

	@Override
	public Set<Long> updateBlocks(final BlockSaveRequest<?> blocks, final ArrayNode blockInfoArray, final Long position) {
		List<SNSBlock> snsBlocks = new ArrayList<>();

		blocks.getSubData().forEach(block -> {
			SNSBlockUpdateRequest request = blockJsonProcessor.convertValue(block, SNSBlockUpdateRequest.class);
			SNSBlock snsBlock = saveOrUpdateSNSBlock(request);
			snsBlocks.add(snsBlock);
		});

		List<SNSBlock> updatedSNSBlocks = snsBlockRepository.saveAll(snsBlocks);

		return updatedSNSBlocks.stream().map(SNSBlock::getId).collect(Collectors.toCollection(LinkedHashSet::new));
	}

	@Override
	public void deleteAllById(final Set<Long> blockIds) {
		snsBlockRepository.deleteAllById(blockIds);
	}

	@Override
	public String getStrategyName() {
		return BlockStrategyName.SNSBlockStrategy.name();
	}
}
