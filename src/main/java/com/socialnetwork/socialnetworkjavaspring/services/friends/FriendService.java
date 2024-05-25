package com.socialnetwork.socialnetworkjavaspring.services.friends;

import com.socialnetwork.socialnetworkjavaspring.DTOs.users.FriendResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.controllers.ApplicationController;
import com.socialnetwork.socialnetworkjavaspring.models.Relation;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.models.enums.RelationType;
import com.socialnetwork.socialnetworkjavaspring.services.relations.IRelationService;
import com.socialnetwork.socialnetworkjavaspring.services.users.IUserService;
import com.socialnetwork.socialnetworkjavaspring.utils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendService implements IFriendService {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRelationService relationService;

    @Override
    public List<FriendResponseDTO> getUserFriends(String userId, User currentUser) {
        List<User> users = userService.findUsersByRelationType(userId, RelationType.FRIEND);
        users = users.stream()
                .filter(u -> !u.getUserId().equals(currentUser.getUserId()))
                .collect(Collectors.toList());

        List<FriendResponseDTO> friendResponseDTOs = ConvertUtils.convertList(users, FriendResponseDTO.class);
        friendResponseDTOs.forEach(friend -> {
            friend.setIsFollowing(false);
            friend.setIsFriend(false);
            friend.setIsRequested(false);
            Long mutualFriend = relationService.countMutualFriends(currentUser.getUserId(), friend.getUserId());
            friend.setMutualFriendCount(mutualFriend);
            List<Relation> relations = relationService.findByUserIdAndUserTargetId(currentUser.getUserId(), friend.getUserId());
            for (Relation relation : relations) {
                if (relation.getType().equals(RelationType.BLOCK)) {
                    friendResponseDTOs.remove(friend);
                    break;
                }
                if (relation.getType().equals(RelationType.FOLLOW)) {
                    friend.setIsFollowing(true);
                }
                if (relation.getType().equals(RelationType.FRIEND)) {
                    friend.setIsFriend(true);
                }
                if (relation.getType().equals(RelationType.REQUEST)) {
                    friend.setIsRequested(true);
                }
            }
        });

        return friendResponseDTOs;
    }
}
