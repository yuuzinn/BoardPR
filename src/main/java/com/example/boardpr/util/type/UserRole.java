package com.example.boardpr.util.type;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"), USER("ROLE_USER");
    UserRole(String value) {
        this.value = value;
    }
    private final String value;
}
