package com.socialnetwork.socialnetworkjavaspring.services.profiles;

import com.socialnetwork.socialnetworkjavaspring.models.Post;
import com.socialnetwork.socialnetworkjavaspring.services.likes.ILikeService;
import com.socialnetwork.socialnetworkjavaspring.services.posts.IPostService;
import com.socialnetwork.socialnetworkjavaspring.services.posts.PostGeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService extends PostGeneralService implements IProfileService {
    @Autowired
    private IPostService postService;

    @Autowired
    private ILikeService likeService;

    @Override
    public List<Post> findAllPostForProfileMe(String userId) {
        List<Post> posts = postService.findAllPostForProfileMe(userId);
        posts.forEach(post -> {
            boolean isLiked = likeService.existsByPostIdAndUserId(post.getPostId(), userId);
            post.setLiked(isLiked);
            setLikedStatusForCommentsAndReplies(post, userId);
            sortCommentsAndReplies(post);
        });
        return posts;
    }

    @Override
    public List<Post> findAllPostForProfile(String userId, boolean isFriend, String userCurrentId) {
        List<Post> posts;
        if (isFriend)
            posts = postService.findAllPostForProfileOther(userId, true);
        else
            posts = postService.findAllPostForProfileOther(userId, false);
        posts.forEach(post -> {
            boolean isLiked = likeService.existsByPostIdAndUserId(post.getPostId(), userCurrentId);
            post.setLiked(isLiked);
            setLikedStatusForCommentsAndReplies(post, userCurrentId);
            sortCommentsAndReplies(post);
        });
        return posts;
    }
}
