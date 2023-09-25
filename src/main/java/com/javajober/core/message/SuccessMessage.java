package com.javajober.core.message;

import lombok.Getter;

@Getter
public enum SuccessMessage {

	HOME_SUCCESS("홈 조회를 성공했습니다.");

	private final String message;

	SuccessMessage(String message) {
		this.message = message;
	}
}
