package com.javajober.spaceWall.dto.request;

import java.util.List;

import com.javajober.styleSetting.dto.request.StyleSettingUpdateRequest;
import com.javajober.wallInfoBlock.dto.request.WallInfoBlockUpdateRequest;

import lombok.Getter;

@Getter
public class DataUpdateRequest {

	private Long memberId;
	private Long addSpaceId;
	private Long spaceWallId;
	private String shareURL;
	private WallInfoBlockUpdateRequest wallInfoBlock;
	private List<BlockRequest> blocks;
	private StyleSettingUpdateRequest styleSetting;

	private DataUpdateRequest(){
	}

	public DataUpdateRequest(final Long spaceWallId, final Long addSpaceId, final Long memberId, final String shareURL, final WallInfoBlockUpdateRequest wallInfoBlock, final List<BlockRequest> blocks, final StyleSettingUpdateRequest styleSetting) {
		this.spaceWallId = spaceWallId;
		this.addSpaceId = addSpaceId;
		this.memberId = memberId;
		this.shareURL = shareURL;
		this.wallInfoBlock = wallInfoBlock;
		this.blocks = blocks;
		this.styleSetting = styleSetting;
	}
}
