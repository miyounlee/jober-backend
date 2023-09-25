package com.javajober.home.dto;

import java.util.List;
import java.util.Map;

import com.javajober.entity.SpaceType;

import lombok.Getter;

@Getter
public class HomeResponse {

	private MemberResponse member;
	private Map<SpaceType, List<AddSpaceResponse>> spaceWall;

	public HomeResponse(final MemberResponse member, final Map<SpaceType, List<AddSpaceResponse>> spaceWall) {
		this.member = member;
		this.spaceWall = spaceWall;
	}
}