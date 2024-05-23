package com.socialnetwork.socialnetworkjavaspring.services.new_feeds;

import com.socialnetwork.socialnetworkjavaspring.models.Post;
import com.socialnetwork.socialnetworkjavaspring.services.likes.ILikeService;
import com.socialnetwork.socialnetworkjavaspring.services.posts.IPostService;
import com.socialnetwork.socialnetworkjavaspring.services.posts.PostGeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsFeedService extends PostGeneralService implements INewsFeedService {
    @Autowired
    private IPostService postService;

    @Autowired
    private ILikeService likeService;


    @Override
    public List<Post> getNewsFeed(String userId) {
        List<Post> posts = postService.findAllPostForNewsFeed(userId);
        posts.forEach(post -> {
            boolean isLiked = likeService.existsByPostIdAndUserId(post.getPostId(), userId);
            post.setLiked(isLiked);
            setLikedStatusForCommentsAndReplies(post, userId);
            sortCommentsAndReplies(post);
        });
        return posts;
    }
}
