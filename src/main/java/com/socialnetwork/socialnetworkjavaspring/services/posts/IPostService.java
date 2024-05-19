package com.socialnetwork.socialnetworkjavaspring.services.posts;

import com.socialnetwork.socialnetworkjavaspring.models.Post;
import com.socialnetwork.socialnetworkjavaspring.services.IGeneralService;

import java.util.List;

public interface IPostService extends IGeneralService<Post, String> {
    List<Post> findAllPostForNewsFeed(String userId);
}
