package com.javajober.entity;

import lombok.Getter;

@Getter
public enum SpaceType {

    PERSONAL("개인"),
    ORGANIZATION("단체");

    private final String description;

    private SpaceType(String description) {
        this.description = description;
    }

}