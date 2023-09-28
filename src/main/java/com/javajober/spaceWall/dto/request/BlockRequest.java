package com.javajober.spaceWall.dto.request;

import java.util.List;

import com.javajober.spaceWall.domain.BlockType;

import lombok.Getter;

@Getter
public class BlockRequest<T> {
	private String blockUUID;
	private BlockType blockType;
	private List<T> subData;

	private BlockRequest() {

	}

	public BlockRequest(final String blockUUID, final BlockType blockType, final List<T> subData) {
		this.blockUUID = blockUUID;
		this.blockType = blockType;
		this.subData = subData;
	}
}
