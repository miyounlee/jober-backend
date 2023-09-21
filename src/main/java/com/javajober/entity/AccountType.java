package com.javajober.entity;

import lombok.Getter;

@Getter
public enum AccountType {

    PERSONAL("개인"),
    ENTERPRISE("기업");

    private final String description;

    private AccountType(String description) {
        this.description = description;
    }
}