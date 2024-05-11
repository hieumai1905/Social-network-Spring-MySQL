package com.socialnetwork.socialnetworkjavaspring.models.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleUser {
    ROLE_ADMIN(0),
    ROLE_USER(1);
    private final int value;
}
