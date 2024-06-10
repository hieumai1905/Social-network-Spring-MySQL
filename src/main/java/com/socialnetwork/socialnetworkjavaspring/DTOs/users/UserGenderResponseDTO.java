package com.socialnetwork.socialnetworkjavaspring.DTOs.users;

import com.socialnetwork.socialnetworkjavaspring.models.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGenderResponseDTO extends UserResponseDTO {
    private Gender gender;
}
