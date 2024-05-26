package com.socialnetwork.socialnetworkjavaspring.DTOs.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseMessageDTO {
    private String userId;
    private String fullName;
    private String avatar;
}
