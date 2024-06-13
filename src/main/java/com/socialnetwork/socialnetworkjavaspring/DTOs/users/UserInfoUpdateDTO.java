package com.socialnetwork.socialnetworkjavaspring.DTOs.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoUpdateDTO {
    private String fullName;
    private String gender;
    private String birthday;
    private String country;
    private String description;
}
