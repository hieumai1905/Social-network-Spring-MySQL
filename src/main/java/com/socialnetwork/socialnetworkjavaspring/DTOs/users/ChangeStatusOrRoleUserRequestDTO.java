package com.socialnetwork.socialnetworkjavaspring.DTOs.users;

import lombok.Data;

@Data
public class ChangeStatusOrRoleUserRequestDTO {
    private String userId;
    private String statusOrRole;
}
