package com.javajober.spaceWall.filedto;

import java.util.List;

import com.javajober.spaceWall.dto.request.BlockSaveRequest;
import com.javajober.blocks.styleSetting.filedto.StyleSettingSaveRequest;
import com.javajober.blocks.wallInfoBlock.filedto.WallInfoBlockSaveRequest;
import lombok.Getter;

@Getter
public class DataSaveRequest {

	private String category;
	private Long spaceId;
	private String shareURL;
	private WallInfoBlockSaveRequest wallInfoBlock;
	private List<BlockSaveRequest<?>> blocks;
	private StyleSettingSaveRequest styleSetting;

	private DataSaveRequest() {

	}

	public DataSaveRequest(final String category, Long spaceId, final String shareURL, final WallInfoBlockSaveRequest wallInfoBlock, final List<BlockSaveRequest<?>> blocks, final StyleSettingSaveRequest styleSetting) {
		this.category = category;
		this.spaceId = spaceId;
		this.shareURL = shareURL;
		this.wallInfoBlock = wallInfoBlock;
		this.blocks = blocks;
		this.styleSetting = styleSetting;
	}
}