package com.javajober.spaceWall.strategy.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.javajober.blocks.wallInfoBlock.dto.request.WallInfoBlockStringUpdateRequest;
import com.javajober.blocks.wallInfoBlock.dto.response.WallInfoBlockResponse;
import com.javajober.blocks.wallInfoBlock.filedto.WallInfoBlockSaveRequest;
import com.javajober.core.util.file.FileImageService;
import com.javajober.core.util.response.CommonResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.javajober.blocks.wallInfoBlock.domain.WallInfoBlock;
import com.javajober.blocks.wallInfoBlock.dto.request.WallInfoBlockStringSaveRequest;
import com.javajober.blocks.wallInfoBlock.repository.WallInfoBlockRepository;
import com.javajober.spaceWall.domain.BlockType;
import com.javajober.spaceWall.dto.request.DataStringSaveRequest;
import com.javajober.spaceWall.dto.request.DataStringUpdateRequest;
import com.javajober.spaceWall.filedto.DataSaveRequest;
import com.javajober.spaceWall.strategy.BlockJsonProcessor;
import com.javajober.spaceWall.strategy.BlockStrategyName;
import com.javajober.spaceWall.strategy.FixBlockStrategy;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class WallInfoBlockStrategy implements FixBlockStrategy {
	private final BlockJsonProcessor blockJsonProcessor;
	private final WallInfoBlockRepository wallInfoBlockRepository;
	private final FileImageService fileImageService;
	private final AtomicReference<String> uploadedBackgroundImgURL = new AtomicReference<>();
	private final AtomicReference<String> uploadedWallInfoImgURL = new AtomicReference<>();

	public WallInfoBlockStrategy(final BlockJsonProcessor blockJsonProcessor, final WallInfoBlockRepository wallInfoBlockRepository,
		FileImageService fileImageService) {
		this.blockJsonProcessor = blockJsonProcessor;
		this.wallInfoBlockRepository = wallInfoBlockRepository;
		this.fileImageService = fileImageService;
	}

	@Override
	public void saveBlocks(final DataSaveRequest data, final ArrayNode blockInfoArray, final Long position) {
		WallInfoBlockSaveRequest request = data.getWallInfoBlock();
		Long wallInfoBlockId = saveWallInfoBlock(request);

		blockJsonProcessor.addBlockInfoToArray(blockInfoArray, position, BlockType.WALL_INFO_BLOCK, wallInfoBlockId,"");
	}

	@Override
	public void uploadTwoFiles(final MultipartFile backgroundImgURL, final MultipartFile wallInfoImgURL) {
		uploadedBackgroundImgURL.set(fileImageService.uploadFile(backgroundImgURL));
		uploadedWallInfoImgURL.set(fileImageService.uploadFile(wallInfoImgURL));
	}

	private Long saveWallInfoBlock(final WallInfoBlockSaveRequest request) {

		WallInfoBlock wallInfoBlock = WallInfoBlockSaveRequest.toEntity(request, uploadedBackgroundImgURL.get(), uploadedWallInfoImgURL.get());
		return wallInfoBlockRepository.save(wallInfoBlock).getId();
	}

	@Override
	public void saveStringBlocks(final DataStringSaveRequest data, final ArrayNode blockInfoArray, final Long position) {
		WallInfoBlockStringSaveRequest request = data.getWallInfoBlock();
		Long wallInfoBlockId = saveWallInfoBlock(request);
		blockJsonProcessor.addBlockInfoToArray(blockInfoArray, position, BlockType.WALL_INFO_BLOCK, wallInfoBlockId,"");
	}

	private Long saveWallInfoBlock(final WallInfoBlockStringSaveRequest request) {

		WallInfoBlock wallInfoBlock = WallInfoBlockStringSaveRequest.toEntity(request);
		return wallInfoBlockRepository.save(wallInfoBlock).getId();
	}


	@Override
	public CommonResponse createFixBlockDTO(final List<JsonNode> fixBlocks) {
		long blockId = fixBlocks.get(0).path("block_id").asLong();
		WallInfoBlock wallInfoBlock = wallInfoBlockRepository.findWallInfoBlock(blockId);

		return WallInfoBlockResponse.from(wallInfoBlock);
	}

	@Override
	public void updateBlocks(final DataStringUpdateRequest data, final ArrayNode blockInfoArray, final Long position) {
		WallInfoBlockStringUpdateRequest wallInfoBlockRequest = data.getWallInfoBlock();
		WallInfoBlock wallInfoBlock = wallInfoBlockRepository.findWallInfoBlock(wallInfoBlockRequest.getWallInfoBlockId());
		wallInfoBlock.update(wallInfoBlockRequest);

		Long wallInfoBlockId = wallInfoBlockRepository.save(wallInfoBlock).getId();

		blockJsonProcessor.addBlockInfoToArray(blockInfoArray, position, BlockType.WALL_INFO_BLOCK, wallInfoBlockId, "");
	}

	@Override
	public String getStrategyName() {
		return BlockStrategyName.WallInfoBlockStrategy.name();
	}
}
