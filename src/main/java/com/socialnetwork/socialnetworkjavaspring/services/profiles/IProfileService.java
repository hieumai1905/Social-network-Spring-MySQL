package com.socialnetwork.socialnetworkjavaspring.services.profiles;

import com.socialnetwork.socialnetworkjavaspring.models.Post;

import java.util.List;

public interface IProfileService {
    List<Post> findAllPostForProfileMe(String userId);

    List<Post> findAllPostForProfile(String userId, boolean isFriend, String userCurrentId);
}
