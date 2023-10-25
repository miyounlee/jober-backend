package com.javajober.spaceWall.strategy.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.javajober.blocks.fileBlock.dto.response.FileBlockResponse;
import com.javajober.core.util.response.CommonResponse;
import org.springframework.stereotype.Component;

import com.javajober.blocks.fileBlock.domain.FileBlock;
import com.javajober.blocks.fileBlock.dto.request.FileBlockStringSaveRequest;
import com.javajober.blocks.fileBlock.repository.FileBlockRepository;
import com.javajober.spaceWall.domain.BlockType;
import com.javajober.spaceWall.dto.request.BlockSaveRequest;
import com.javajober.spaceWall.strategy.BlockJsonProcessor;
import com.javajober.spaceWall.strategy.BlockStrategyName;
import com.javajober.spaceWall.strategy.MoveBlockStrategy;

@Component
public class FileBlockStrategy implements MoveBlockStrategy {
	private final BlockJsonProcessor blockJsonProcessor;
	private final FileBlockRepository fileBlockRepository;

	public FileBlockStrategy(BlockJsonProcessor blockJsonProcessor, FileBlockRepository fileBlockRepository) {
		this.blockJsonProcessor = blockJsonProcessor;
		this.fileBlockRepository = fileBlockRepository;
	}

	@Override
	public void saveBlocks(final BlockSaveRequest<?> block, final ArrayNode blockInfoArray, final Long position) {

		List<FileBlockStringSaveRequest> fileBlockRequests = convertSubDataToFileBlockSaveRequests(block.getSubData());

		List<FileBlock> fileBlocks = convertToFileBlocks(fileBlockRequests);

		List<FileBlock> savedFileBlocks = saveAllFileBlock(fileBlocks);

		addToFileBlockInfoArray(savedFileBlocks, blockInfoArray, position, block.getBlockType());
	}

	private List<FileBlockStringSaveRequest> convertSubDataToFileBlockSaveRequests(final List<?> subData) {
		List<FileBlockStringSaveRequest> fileBlockRequests = new ArrayList<>();

		subData.forEach(block -> {
			FileBlockStringSaveRequest request = blockJsonProcessor.convertValue(block, FileBlockStringSaveRequest.class);
			fileBlockRequests.add(request);
		});
		return fileBlockRequests;
	}

	private List<FileBlock> convertToFileBlocks(final List<FileBlockStringSaveRequest> fileBlockSaveRequests) {
		return fileBlockSaveRequests.stream()
			.map(FileBlockStringSaveRequest::toEntity)
			.collect(Collectors.toList());
	}

	private List<FileBlock> saveAllFileBlock(final List<FileBlock> fileBlocks) {
		return fileBlockRepository.saveAll(fileBlocks);
	}

	private void addToFileBlockInfoArray (final List<FileBlock> savedFileBlocks, final ArrayNode blockInfoArray, final Long position, final String fileBlockUUID) {
		savedFileBlocks.forEach(savedFileBlock ->
			blockJsonProcessor.addBlockInfoToArray(blockInfoArray, position, BlockType.FILE_BLOCK, savedFileBlock.getId(), fileBlockUUID)
		);
	}

	@Override
	public List<CommonResponse> createMoveBlockDTO(final List<JsonNode> blocksWithSamePosition) {
		List<CommonResponse> subData = new ArrayList<>();
		for (JsonNode block : blocksWithSamePosition) {
			long blockId = block.path("block_id").asLong();
			FileBlock fileBlock = fileBlockRepository.findFileBlock(blockId);
			subData.add(FileBlockResponse.from(fileBlock));
		}
		return subData;
	}

	@Override
	public String getStrategyName() {
		return BlockStrategyName.FileBlockStrategy.name();
	}
}
