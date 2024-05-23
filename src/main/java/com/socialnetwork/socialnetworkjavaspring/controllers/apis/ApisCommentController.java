package com.socialnetwork.socialnetworkjavaspring.controllers.apis;

import com.socialnetwork.socialnetworkjavaspring.DTOs.comments.CommentRequestDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.comments.CommentResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.controllers.ApplicationController;
import com.socialnetwork.socialnetworkjavaspring.models.Comment;
import com.socialnetwork.socialnetworkjavaspring.models.Post;
import com.socialnetwork.socialnetworkjavaspring.services.comments.ICommentService;
import com.socialnetwork.socialnetworkjavaspring.services.posts.IPostService;
import com.socialnetwork.socialnetworkjavaspring.utils.ConvertUtils;
import com.socialnetwork.socialnetworkjavaspring.utils.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class ApisCommentController extends ApplicationController {
    @Autowired
    protected IPostService postService;

    @Autowired
    protected ICommentService commentService;

    @GetMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse> getAllByPostId(@PathVariable("postId") String postId) {
        Optional<Post> post = postService.findById(postId);
        if (post.isEmpty()) {
            return responseApi(HttpStatus.NOT_FOUND, "Post not found");
        }
        List<Comment> comments = commentService.findAllByPostId(postId);
        List<CommentResponseDTO> commentResponseDTOs = ConvertUtils.convertList(comments, CommentResponseDTO.class);
        return responseApi(HttpStatus.OK, String.format("Get all comments by post id: %s successfully", postId), commentResponseDTOs);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody CommentRequestDTO commentRequestDTO) {
        Optional<Post> post = postService.findById(commentRequestDTO.getPostId());
        if (post.isEmpty()) {
            return responseApi(HttpStatus.NOT_FOUND, "Post not found");
        }
        Comment newComment = new Comment(commentRequestDTO.getContent(), currentUser, post.get());
        Optional<Comment> commentAdded = commentService.save(newComment);
        if (commentAdded.isEmpty()) {
            return responseApi(HttpStatus.NOT_FOUND, "Create comment failed");
        }
        CommentResponseDTO commentResponseDTO = ConvertUtils.convert(commentAdded.get(), CommentResponseDTO.class);
        return responseApi(HttpStatus.CREATED,
                "Create comment successfully", commentResponseDTO);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse> update(@PathVariable("commentId") Long commentId, @RequestBody CommentRequestDTO commentRequestDTO) {
        Optional<Comment> comment = commentService.findById(commentId);
        if (comment.isEmpty()) {
            return responseApi(HttpStatus.NOT_FOUND, "Comment not found");
        }
        comment.get().setContent(commentRequestDTO.getContent());
        Optional<Comment> commentUpdated = commentService.save(comment.get());
        if (commentUpdated.isEmpty()) {
            return responseApi(HttpStatus.NOT_FOUND, "Update comment failed");
        }
        CommentResponseDTO commentResponseDTO = ConvertUtils.convert(commentUpdated.get(), CommentResponseDTO.class);
        return responseApi(HttpStatus.OK, String.format("Update comment %s successfully", commentId), commentResponseDTO);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("commentId") Long commentId) {
        Optional<Comment> comment = commentService.findById(commentId);
        if (comment.isEmpty()) {
            return responseApi(HttpStatus.NOT_FOUND, "Comment not found");
        }
        Optional<Comment> commentDeleted = commentService.delete(comment.get());
        String postId = commentDeleted.get().getPost().getPostId();
        return responseApi(HttpStatus.NO_CONTENT, String.format("Delete comment %s of post %s successfully", commentId, postId), postId);
    }
}
