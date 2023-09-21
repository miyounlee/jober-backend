package com.javajober.entity;

import lombok.Getter;

@Getter
public enum SnsType {

    INSTAGRAM("인스타그램"),
    FACEBOOK("페이스북"),
    LINKEDIN("링크드인"),
    BEHANCE("비핸스");

    private final String description;

    private SnsType(final String description) {
        this.description = description;
    }
}

