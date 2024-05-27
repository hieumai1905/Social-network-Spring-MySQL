package com.socialnetwork.socialnetworkjavaspring.services.new_feeds;

import com.socialnetwork.socialnetworkjavaspring.models.Post;
import com.socialnetwork.socialnetworkjavaspring.models.PostInteract;
import com.socialnetwork.socialnetworkjavaspring.models.enums.InteractType;
import com.socialnetwork.socialnetworkjavaspring.repositories.IPostRepository;
import com.socialnetwork.socialnetworkjavaspring.services.likes.ILikeService;
import com.socialnetwork.socialnetworkjavaspring.services.post_interacts.IPostInteractService;
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

    @Autowired
    private IPostInteractService postInteractService;

    @Autowired
    private IPostRepository postRepository;


    @Override
    public List<Post> getNewsFeed(String userId) {
        List<Post> posts = postService.findAllPostForNewsFeed(userId);
        posts.forEach(post -> {
            boolean isLiked = likeService.existsByPostIdAndUserId(post.getPostId(), userId);
            PostInteract savedPostInteract = postInteractService.checkExistPostInteract(post, InteractType.SAVED, userId);
            post.setLiked(isLiked);
            post.setSaved(savedPostInteract != null);
            setLikedStatusForCommentsAndReplies(post, userId);
            sortCommentsAndReplies(post);
        });
        return posts;
    }

    @Override
    public Post findById(String postId, String userId) {
        Post post = postRepository.findPostByIdAndNotHiddenOrReportedOrBlockedOrPrivate(postId, userId).orElseThrow(() ->
                new RuntimeException("Post not found"));
        boolean isLiked = likeService.existsByPostIdAndUserId(post.getPostId(), userId);
        PostInteract savedPostInteract = postInteractService.checkExistPostInteract(post, InteractType.SAVED, userId);
        post.setLiked(isLiked);
        post.setSaved(savedPostInteract != null);
        setLikedStatusForCommentsAndReplies(post, userId);
        sortCommentsAndReplies(post);
        return post;
    }
}
