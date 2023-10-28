package com.javajober.spaceWall.dto.request;

import lombok.Getter;

@Getter
public class SpaceWallStringUpdateRequest {

	private DataStringUpdateRequest data;

	private SpaceWallStringUpdateRequest() {
	}

	public SpaceWallStringUpdateRequest(final DataStringUpdateRequest data) {
		this.data = data;
	}
}