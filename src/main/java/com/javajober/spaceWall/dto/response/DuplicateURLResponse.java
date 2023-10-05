package com.javajober.spaceWall.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DuplicateURLResponse {

	private Boolean hasDuplicateURL;

	private DuplicateURLResponse() {}

	@Builder
	public DuplicateURLResponse(Boolean hasDuplicateURL) {
		this.hasDuplicateURL = hasDuplicateURL;
	}

	public static DuplicateURLResponse from (Boolean hasDuplicateURL) {
		return DuplicateURLResponse.builder()
			.hasDuplicateURL(hasDuplicateURL)
			.build();
	}
}
