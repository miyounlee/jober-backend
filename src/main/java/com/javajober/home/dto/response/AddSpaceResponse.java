package com.javajober.home.dto.response;

import com.javajober.space.domain.AddSpace;
import com.javajober.space.domain.SpaceType;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AddSpaceResponse {

	private Long addSpaceId;
	private SpaceType spaceType;
	private String spaceTitle;

	private AddSpaceResponse() {

	}

	@Builder
	public AddSpaceResponse(final Long addSpaceId, final SpaceType spaceType, final String spaceTitle) {
		this.addSpaceId = addSpaceId;
		this.spaceType = spaceType;
		this.spaceTitle = spaceTitle;
	}

	public static AddSpaceResponse from(final AddSpace addSpace) {
		return AddSpaceResponse.builder()
			.addSpaceId(addSpace.getId())
			.spaceType(addSpace.getSpaceType())
			.spaceTitle(addSpace.getSpaceTitle())
			.build();
	}
}