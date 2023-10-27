package com.javajober.home.dto.response;

import java.util.List;
import java.util.Map;

import com.javajober.space.domain.SpaceType;

import lombok.Builder;
import lombok.Getter;

@Getter
public class HomeResponse {

	private MemberResponse member;
	private Map<SpaceType, List<AddSpaceResponse>> space;

	@Builder
	public HomeResponse(final MemberResponse member, final Map<SpaceType, List<AddSpaceResponse>> space) {
		this.member = member;
		this.space = space;
	}
}