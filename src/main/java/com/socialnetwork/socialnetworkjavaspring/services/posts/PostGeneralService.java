package com.socialnetwork.socialnetworkjavaspring.services.posts;

import com.socialnetwork.socialnetworkjavaspring.models.Comment;
import com.socialnetwork.socialnetworkjavaspring.models.CommentReply;
import com.socialnetwork.socialnetworkjavaspring.models.Post;
import com.socialnetwork.socialnetworkjavaspring.services.likes.ILikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public abstract class PostGeneralService {
    @Autowired
    private ILikeService likeService;

    protected void setLikedStatusForCommentsAndReplies(Post post, String userId) {
        post.getComments().forEach(comment -> {
            boolean isLikedComment = likeService.existsByCommentIdAndUserId(comment.getCommentId(), userId);
            comment.setLiked(isLikedComment);
            comment.getCommentReplies().forEach(reply -> {
                boolean isLikedReply = likeService.existsByCommentReplyIdAndUserId(reply.getCommentReplyId(), userId);
                reply.setLiked(isLikedReply);
            });
        });
    }

    protected void sortCommentsAndReplies(Post post) {
        post.getComments().sort(Comparator.comparing(Comment::getCommentAt));
        post.getComments().forEach(comment -> comment.getCommentReplies()
                .sort(Comparator.comparing(CommentReply::getReplyAt)));
    }
}
