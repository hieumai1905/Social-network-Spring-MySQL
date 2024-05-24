package com.socialnetwork.socialnetworkjavaspring.DTOs.relations;

import com.socialnetwork.socialnetworkjavaspring.DTOs.users.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationResponseDTO {
    private String setAt;
    private UserResponseDTO user;
    private Long mutualFriendCount;
}
