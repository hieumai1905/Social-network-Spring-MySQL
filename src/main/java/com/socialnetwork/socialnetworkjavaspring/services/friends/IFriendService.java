package com.socialnetwork.socialnetworkjavaspring.services.friends;

import com.socialnetwork.socialnetworkjavaspring.DTOs.users.FriendResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.models.User;

import java.util.List;

public interface IFriendService {
    List<FriendResponseDTO> getUserFriends(String userId, User currentUser);
}
