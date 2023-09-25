package com.javajober.core.message;

import lombok.Getter;

@Getter
public enum ErrorMessage {

	MEMBER_NOT_FOUND("존재하지 않는 회원 정보입니다."),
	ADD_SPACE_NOT_FOUND("존재하지 않는 스페이스 입니다."),

	FAILED_TO_DELETE_THE_FILE("파일 삭제를 실패하였습니다."),
	SAVED_SPACE_WALL_ALREADY_EXISTS("저장된 공유페이지가 있습니다."),
	;

	private final String message;

	ErrorMessage(String message) {
		this.message = message;
	}
}
