package com.javajober.spaceWall.strategy.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.javajober.blocks.fileBlock.dto.response.FileBlockResponse;
import com.javajober.blocks.fileBlock.filedto.FileBlockSaveRequest;
import com.javajober.core.util.file.FileImageService;
import com.javajober.core.util.response.CommonResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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
	private final FileImageService fileImageService;
	private final AtomicReference<String> currentFileName = new AtomicReference<>();

	public FileBlockStrategy(BlockJsonProcessor blockJsonProcessor, FileBlockRepository fileBlockRepository,
		FileImageService fileImageService) {
		this.blockJsonProcessor = blockJsonProcessor;
		this.fileBlockRepository = fileBlockRepository;
		this.fileImageService = fileImageService;
	}

	@Override
	public void saveBlocks(BlockSaveRequest<?> block, ArrayNode blockInfoArray, Long position) {

		List<FileBlockSaveRequest> fileBlockRequests = convertSubDataToFileBlockSaveRequests(block.getSubData());

		List<FileBlock> fileBlocks = convertToFileBlocks(fileBlockRequests,  currentFileName.get());

		List<FileBlock> savedFileBlocks = saveAllFileBlock(fileBlocks);

		addToFileBlockInfoArray(savedFileBlocks, blockInfoArray, position, block.getBlockType());
	}

	@Override
	public void uploadFile (final MultipartFile file) {
		currentFileName.set(fileImageService.uploadFile(file));
	}

	private List<FileBlockSaveRequest> convertSubDataToFileBlockSaveRequests(final List<?> subData) {
		List<FileBlockSaveRequest> fileBlockRequests = new ArrayList<>();

		subData.forEach(block -> {
			FileBlockSaveRequest request = blockJsonProcessor.convertValue(block, FileBlockSaveRequest.class);
			fileBlockRequests.add(request);
		});
		return fileBlockRequests;
	}

	private List<FileBlock> convertToFileBlocks(final List<FileBlockSaveRequest> fileBlockSaveRequests, String fileName) {
		return fileBlockSaveRequests.stream()
			.map(fileBlockSaveRequest -> FileBlockSaveRequest.toEntity(fileBlockSaveRequest, fileName))
			.collect(Collectors.toList());
	}

	@Override
	public void saveStringBlocks(final BlockSaveRequest<?> block, final ArrayNode blockInfoArray, final Long position) {

		List<FileBlockStringSaveRequest> fileBlockStringSaveRequests = convertSubDataToFileBlockStringSaveRequests(block.getSubData());

		List<FileBlock> stringFileBlocks = convertToFileBlocks(fileBlockStringSaveRequests);

		List<FileBlock> stringSavedFileBlocks = saveAllFileBlock(stringFileBlocks);

		addToFileBlockInfoArray(stringSavedFileBlocks, blockInfoArray, position, block.getBlockType());

	}

	private List<FileBlockStringSaveRequest> convertSubDataToFileBlockStringSaveRequests(final List<?> subData) {
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
