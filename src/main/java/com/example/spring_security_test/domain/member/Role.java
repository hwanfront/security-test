package com.example.spring_security_test.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("ROLE_USER", "common user"),
    ADMIN("ROLE_ADMIN", "common administrator"),
    GUEST("ROLE_GUEST", "guest user");

    private final String key;
    private final String title;
}
