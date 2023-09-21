package com.javajober.entity;

import lombok.Getter;

@Getter
public enum SpaceWallCategoryType {

    CAREER("취업/이직"),
    PERSONAL("개인/소개"),
    EVENT("이벤트/일상"),
    ENTERPRISE("기업/근로양식"),
    BASIC("기본 템플릿");

    private final String description;

    SpaceWallCategoryType(String description) {
        this.description = description;
    }
}
