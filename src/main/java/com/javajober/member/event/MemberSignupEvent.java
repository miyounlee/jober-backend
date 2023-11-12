package com.javajober.member.event;

import com.javajober.member.domain.Member;

import lombok.Getter;

@Getter
public class MemberSignupEvent {

	private final Member member;

	public MemberSignupEvent(final Member member) {
		this.member = member;
	}
}
