package com.socialnetwork.socialnetworkjavaspring.services.new_feeds;

import com.socialnetwork.socialnetworkjavaspring.models.Post;
import com.socialnetwork.socialnetworkjavaspring.models.User;

import java.util.List;

public interface INewsFeedService {
    List<Post> getNewsFeed(String userId);

    Post findById(String postId, User user);

    Post findByIdCurrentUser(String postId, String userId);
}
