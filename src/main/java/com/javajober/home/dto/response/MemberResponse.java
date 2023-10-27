package com.javajober.home.dto.response;

import com.javajober.member.domain.Member;
import com.javajober.member.domain.MemberShipType;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponse {

	private String memberName;
	private String memberProfileImageUrl;
	private MemberShipType memberShip;

	private MemberResponse() {

	}

	@Builder
	public MemberResponse(final String memberName, final String memberProfileImageUrl, final MemberShipType memberShip) {
		this.memberName = memberName;
		this.memberProfileImageUrl = memberProfileImageUrl;
		this.memberShip = memberShip;
	}

	public static MemberResponse from(final Member member) {

		return MemberResponse.builder()
			.memberName(member.getMemberName())
			.memberProfileImageUrl(member.getMemberProfileImageUrl())
			.memberShip(member.getMemberShip())
			.build();
	}

	public String getMemberShip() {

		if (this.memberShip == null) {
			return null;
		}

		String name = this.memberShip.name().toLowerCase();

		return Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}
}