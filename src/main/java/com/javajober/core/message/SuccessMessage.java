package com.javajober.core.message;

import lombok.Getter;

@Getter
public enum SuccessMessage {

	HOME_SUCCESS("홈 조회를 성공했습니다."),
	SUCCESS_TEMPORARY_SAVE_QUERY("임시 저장 조회를 성공했습니다."),
	;

	private final String message;

	SuccessMessage(String message) {
		this.message = message;
	}
}
