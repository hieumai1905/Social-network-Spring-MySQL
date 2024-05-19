package com.socialnetwork.socialnetworkjavaspring.services.new_feeds;

import com.socialnetwork.socialnetworkjavaspring.models.Post;
import com.socialnetwork.socialnetworkjavaspring.models.Comment;
import com.socialnetwork.socialnetworkjavaspring.models.CommentReply;
import com.socialnetwork.socialnetworkjavaspring.services.like.ILikeService;
import com.socialnetwork.socialnetworkjavaspring.services.posts.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class NewsFeedService implements INewsFeedService {
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

    private void setLikedStatusForCommentsAndReplies(Post post, String userId) {
        post.getComments().forEach(comment -> {
            boolean isLikedComment = likeService.existsByCommentIdAndUserId(comment.getCommentId(), userId);
            comment.setLiked(isLikedComment);
            comment.getCommentReplies().forEach(reply -> {
                boolean isLikedReply = likeService.existsByCommentReplyIdAndUserId(reply.getCommentReplyId(), userId);
                reply.setLiked(isLikedReply);
            });
        });
    }

    private void sortCommentsAndReplies(Post post) {
        post.getComments().sort(Comparator.comparing(Comment::getCommentAt));
        post.getComments().forEach(comment -> comment.getCommentReplies()
                .sort(Comparator.comparing(CommentReply::getReplyAt)));
    }
}
