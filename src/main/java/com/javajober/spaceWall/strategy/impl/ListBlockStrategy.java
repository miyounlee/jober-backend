package com.javajober.spaceWall.strategy.impl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.javajober.blocks.listBlock.dto.request.ListBlockUpdateRequest;
import com.javajober.blocks.listBlock.dto.response.ListBlockResponse;
import com.javajober.core.util.response.CommonResponse;
import org.springframework.stereotype.Component;

import com.javajober.blocks.listBlock.domain.ListBlock;
import com.javajober.blocks.listBlock.dto.request.ListBlockSaveRequest;
import com.javajober.blocks.listBlock.repository.ListBlockRepository;
import com.javajober.spaceWall.domain.BlockType;
import com.javajober.spaceWall.dto.request.BlockSaveRequest;
import com.javajober.spaceWall.strategy.BlockJsonProcessor;
import com.javajober.spaceWall.strategy.BlockStrategyName;
import com.javajober.spaceWall.strategy.MoveBlockStrategy;

@Component
public class ListBlockStrategy implements MoveBlockStrategy {

	private final BlockJsonProcessor blockJsonProcessor;
	private final ListBlockRepository listBlockRepository;

	public ListBlockStrategy(final BlockJsonProcessor blockJsonProcessor, final ListBlockRepository listBlockRepository) {
		this.blockJsonProcessor = blockJsonProcessor;
		this.listBlockRepository = listBlockRepository;
	}

	@Override
	public void saveStringBlocks(final BlockSaveRequest<?> block, final ArrayNode blockInfoArray, final Long position) {

		List<ListBlockSaveRequest> listBlockRequests = convertSubDataToListBlockSaveRequests(block.getSubData());

		List<ListBlock> listBlocks = convertToListBlocks(listBlockRequests);

		List<ListBlock> savedListBlocks = saveAllListBlock(listBlocks);

		addToListBlockInfoArray(savedListBlocks, blockInfoArray, position, block.getBlockUUID());
	}

	@Override
	public void saveBlocks(final BlockSaveRequest<?> block, final ArrayNode blockInfoArray, final Long position) {
		List<ListBlockSaveRequest> listBlockRequests = convertSubDataToListBlockSaveRequests(block.getSubData());

		List<ListBlock> listBlocks = convertToListBlocks(listBlockRequests);

		List<ListBlock> savedListBlocks = saveAllListBlock(listBlocks);

		addToListBlockInfoArray(savedListBlocks, blockInfoArray, position, block.getBlockUUID());
	}

	private List<ListBlockSaveRequest> convertSubDataToListBlockSaveRequests(final List<?> subData) {
		List<ListBlockSaveRequest> listBlockRequests = new ArrayList<>();

		subData.forEach(block -> {
			ListBlockSaveRequest request = blockJsonProcessor.convertValue(block, ListBlockSaveRequest.class);
			listBlockRequests.add(request);
		});
		return listBlockRequests;
	}

	private List<ListBlock> convertToListBlocks(final List<ListBlockSaveRequest> listBlockRequests) {
		return listBlockRequests.stream()
			.map(ListBlockSaveRequest::toEntity)
			.collect(Collectors.toList());
	}

	private List<ListBlock> saveAllListBlock(final List<ListBlock> listBlocks) {
		return listBlockRepository.saveAll(listBlocks);
	}

	private void addToListBlockInfoArray (final List<ListBlock> savedListBlocks, final ArrayNode blockInfoArray, final Long position, final String listBlockUUID) {
		savedListBlocks.forEach(savedListBlock ->
			blockJsonProcessor.addBlockInfoToArray(blockInfoArray, position, BlockType.LIST_BLOCK, savedListBlock.getId(), listBlockUUID)
		);
	}

	@Override
	public List<CommonResponse> createMoveBlockDTO(final List<JsonNode> blocksWithSamePosition) {
		List<CommonResponse> subData = new ArrayList<>();
		for (JsonNode block : blocksWithSamePosition) {
			long blockId = block.path("block_id").asLong();
			ListBlock listBlock = listBlockRepository.findListBlock(blockId);
			subData.add(ListBlockResponse.from(listBlock));
		}
		return subData;
	}

	@Override
	public Set<Long> updateStringBlocks(final BlockSaveRequest<?> blocks, final ArrayNode blockInfoArray, final Long position) {

		List<ListBlock> listBlocks = new ArrayList<>();

		blocks.getSubData().forEach(block -> {
			ListBlockUpdateRequest request = blockJsonProcessor.convertValue(block, ListBlockUpdateRequest.class);
			ListBlock listBlock = saveOrUpdateListBlock(request);
			listBlocks.add(listBlock);
		});

		List<ListBlock> updatedListBlocks = listBlockRepository.saveAll(listBlocks);

		return updatedListBlocks.stream().map(ListBlock::getId).collect(Collectors.toCollection(LinkedHashSet::new));
	}

	private ListBlock saveOrUpdateListBlock (final ListBlockUpdateRequest request) {

		if(request.getListBlockId() == null) {
			return ListBlockUpdateRequest.toEntity(request);
		}

		ListBlock listBlock = listBlockRepository.findListBlock(request.getListBlockId());
		listBlock.update(request);
		return listBlock;
	}

	@Override
	public Set<Long> updateBlocks(final BlockSaveRequest<?> blocks, final ArrayNode blockInfoArray, final Long position) {
		List<ListBlock> listBlocks = new ArrayList<>();

		blocks.getSubData().forEach(block -> {
			ListBlockUpdateRequest request = blockJsonProcessor.convertValue(block, ListBlockUpdateRequest.class);
			ListBlock listBlock = saveOrUpdateListBlock(request);
			listBlocks.add(listBlock);
		});

		List<ListBlock> updatedListBlocks = listBlockRepository.saveAll(listBlocks);

		return updatedListBlocks.stream().map(ListBlock::getId).collect(Collectors.toCollection(LinkedHashSet::new));
	}

	@Override
	public void deleteAllById(final Set<Long> blockIds) {
		listBlockRepository.deleteAllById(blockIds);
	}

	@Override
	public String getStrategyName() {
		return BlockStrategyName.ListBlockStrategy.name();
	}
}
