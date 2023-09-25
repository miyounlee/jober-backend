package com.javajober.core.message;

import lombok.Getter;

@Getter
public enum ErrorMessage {

	MEMBER_NOT_FOUND("존재하지 않는 회원 정보입니다."),
	ADD_SPACE_NOT_FOUND("존재하지 않는 스페이스 입니다."),

	private final String message;

	ErrorMessage(String message) {
		this.message = message;
	}
}