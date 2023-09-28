package com.javajober.spaceWall.dto.request;

import java.util.List;

import lombok.Getter;

@Getter
public class DataRequest {
	private String category;
	private Long memberId;
	private String shareURL;
	private List<BlockRequest> blocks;
	private DataRequest() {
	}

	public DataRequest(final String category, final Long memberId, final String shareURL, final List<BlockRequest> blocks) {
		this.category = category;
		this.memberId = memberId;
		this.shareURL = shareURL;
		this.blocks = blocks;
	}
}