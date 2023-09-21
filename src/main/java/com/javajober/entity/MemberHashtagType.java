package com.javajober.entity;

import lombok.Getter;

@Getter
public enum MemberHashtagType {

    FRIEND("친구"),
    COLLEAGUE("직장동료");

    private final String description;

    private MemberHashtagType(String description) {
        this.description = description;
    }
}
