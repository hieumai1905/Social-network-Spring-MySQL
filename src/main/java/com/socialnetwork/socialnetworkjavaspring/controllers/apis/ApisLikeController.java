package com.socialnetwork.socialnetworkjavaspring.controllers.apis;

import com.socialnetwork.socialnetworkjavaspring.controllers.ApplicationController;
import com.socialnetwork.socialnetworkjavaspring.models.Comment;
import com.socialnetwork.socialnetworkjavaspring.models.CommentReply;
import com.socialnetwork.socialnetworkjavaspring.models.Like;
import com.socialnetwork.socialnetworkjavaspring.models.Post;
import com.socialnetwork.socialnetworkjavaspring.services.comment_replies.ICommentReplyService;
import com.socialnetwork.socialnetworkjavaspring.services.comments.ICommentService;
import com.socialnetwork.socialnetworkjavaspring.services.likes.ILikeService;
import com.socialnetwork.socialnetworkjavaspring.services.posts.IPostService;
import com.socialnetwork.socialnetworkjavaspring.utils.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.model.IComment;

import java.util.Optional;

@RestController
@RequestMapping("/api/likes")
public class ApisLikeController extends ApplicationController {
    @Autowired
    private ILikeService likeService;

    @Autowired
    private IPostService postService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private ICommentReplyService commentReplyService;

    @PostMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse> likePost(@PathVariable("postId") String postId) {
        Optional<Like> likeExits = likeService.findByPostIdAndUserId(postId, currentUser.getUserId());
        if (likeExits.isPresent()) {
            likeService.delete(likeExits.get());
            return responseApi(HttpStatus.NO_CONTENT, "Unliked post successfully");
        }

        Optional<Post> post = postService.findById(postId);
        if (post.isEmpty()) {
            return responseApi(HttpStatus.NOT_FOUND, "Post not found");
        }

        Like like = new Like(currentUser, post.get());
        likeService.save(like);
        return responseApi(HttpStatus.CREATED, "Liked post successfully");
    }

    @PostMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse> likeComment(@PathVariable("commentId") Long commentId) {
        Optional<Like> likesExits = likeService.findByCommentIdAndUserId(commentId, currentUser.getUserId());
        if (likesExits.isPresent()) {
            likeService.delete(likesExits.get());
            return responseApi(HttpStatus.NO_CONTENT, "Unliked comment successfully");
        }

        Optional<Comment> comment = commentService.findById(commentId);
        if (comment.isEmpty()) {
            return responseApi(HttpStatus.NOT_FOUND, "Comment not found");
        }

        Like like = new Like(currentUser, comment.get());
        likeService.save(like);
        return responseApi(HttpStatus.CREATED, "Liked comment successfully");
    }

    @PostMapping("/comment-replies/{commentReplyId}")
    public ResponseEntity<ApiResponse> likeCommentReply(@PathVariable("commentReplyId") Long commentReplyId) {
        Optional<Like> likesExits = likeService.findByCommentReplyIdAndUserId(commentReplyId, currentUser.getUserId());
        if (likesExits.isPresent()) {
            likeService.delete(likesExits.get());
            return responseApi(HttpStatus.NO_CONTENT, "Unliked comment reply successfully");
        }

        Optional<CommentReply> commentReply = commentReplyService.findById(commentReplyId);
        if (commentReply.isEmpty()) {
            return responseApi(HttpStatus.NOT_FOUND, "Comment reply not found");
        }

        Like like = new Like(currentUser, commentReply.get());
        likeService.save(like);
        return responseApi(HttpStatus.CREATED, "Liked comment reply successfully");
    }
}
