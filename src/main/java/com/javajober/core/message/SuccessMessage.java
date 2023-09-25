package com.javajober.core.message;

import lombok.Getter;

@Getter
public enum SuccessMessage {

	HOME_SUCCESS("홈 조회를 성공했습니다."),
	SUCCESS_TEMPORARY_SAVE_QUERY("임시 저장 조회를 성공했습니다."),

	TEMPLATE_AUTH_SUCCESS("권한설정을 위한 유저정보 조회를 성공했습니다."),
	TEMPLATE_RECOMMEND_SUCCESS("추천템플릿 조회를 성공했습니다."),
	TEMPLATE_SEARCH_SUCCESS("템플릿 검색을 성공했습니다."),
	TEMPLATE_CATEGORY_SUCCESS("카테고리별 템플릿 리스트 조회를 성공했습니다.");

	private final String message;

	SuccessMessage(String message) {
		this.message = message;
	}
}
