package com.javajober.spaceWall.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SpaceWallSaveResponse {

	private Long spaceWallId;

	private SpaceWallSaveResponse() {

	}

	@Builder
	public SpaceWallSaveResponse(Long spaceWallId) {
		this.spaceWallId = spaceWallId;
	}

}
