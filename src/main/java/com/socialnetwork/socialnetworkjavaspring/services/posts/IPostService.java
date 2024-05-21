package com.socialnetwork.socialnetworkjavaspring.services.posts;

import com.socialnetwork.socialnetworkjavaspring.models.Post;
import com.socialnetwork.socialnetworkjavaspring.services.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface IPostService extends IGeneralService<Post, String> {
    List<Post> findAllPostForNewsFeed(String userId);

    Optional<Post> findById(String postId);
}
