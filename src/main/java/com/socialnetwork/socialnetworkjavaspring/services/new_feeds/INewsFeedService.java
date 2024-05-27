package com.socialnetwork.socialnetworkjavaspring.services.new_feeds;

import com.socialnetwork.socialnetworkjavaspring.models.Post;

import java.util.List;

public interface INewsFeedService {
    List<Post> getNewsFeed(String userId);

    Post findById(String postId, String userId);
}
