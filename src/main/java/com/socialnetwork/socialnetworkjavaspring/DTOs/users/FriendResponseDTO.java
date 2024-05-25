package com.socialnetwork.socialnetworkjavaspring.DTOs.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FriendResponseDTO extends UserResponseDTO {
    private Boolean isRequested;
    private Boolean isFollowing;
    private Long mutualFriendCount;
}
