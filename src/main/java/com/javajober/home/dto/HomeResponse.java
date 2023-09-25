package com.javajober.home.dto;

import java.util.List;
import java.util.Map;

import lombok.Getter;

@Getter
public class HomeResponse {

	private MemberResponse member;
	private Map<String, List<AddSpaceResponse>> spaceWall;

	public HomeResponse(final MemberResponse member, final Map<String, List<AddSpaceResponse>> spaceWall) {
		this.member = member;
		this.spaceWall = spaceWall;
	}
}