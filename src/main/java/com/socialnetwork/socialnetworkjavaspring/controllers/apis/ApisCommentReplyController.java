package com.socialnetwork.socialnetworkjavaspring.controllers.apis;

import com.socialnetwork.socialnetworkjavaspring.DTOs.comments.CommentReplyRequestDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.comments.CommentReplyResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.controllers.ApplicationController;
import com.socialnetwork.socialnetworkjavaspring.models.Comment;
import com.socialnetwork.socialnetworkjavaspring.models.CommentReply;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.services.comment_replies.ICommentReplyService;
import com.socialnetwork.socialnetworkjavaspring.services.comments.ICommentService;
import com.socialnetwork.socialnetworkjavaspring.utils.ConvertUtils;
import com.socialnetwork.socialnetworkjavaspring.utils.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments/replies")
public class ApisCommentReplyController extends ApplicationController {
    @Autowired
    protected ICommentService commentService;

    @Autowired
    private ICommentReplyService commentReplyService;

    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody CommentReplyRequestDTO commentReplyRequestDTO) {
        CommentReply newCommentReply = new CommentReply();
        newCommentReply.setContent(commentReplyRequestDTO.getContent());
        Optional<Comment> comment = commentService.findById(commentReplyRequestDTO.getCommentId());
        if (comment.isEmpty()) {
            return responseApi(HttpStatus.NOT_FOUND, "Comment not found");
        }
        newCommentReply.setComment(comment.get());
        User user = currentUser;
        newCommentReply.setUser(user);
        newCommentReply.setReplyAt(new Date());
        Optional<CommentReply> commentReplyAdded = commentReplyService.save(newCommentReply);
        if (commentReplyAdded.isEmpty()) {
            return responseApi(HttpStatus.NOT_FOUND, "Create comment reply failed");
        }
        CommentReplyResponseDTO commentResponseDTO = ConvertUtils.convert(commentReplyAdded.get(), CommentReplyResponseDTO.class);
        return responseApi(HttpStatus.CREATED, "Create comment reply successfully", commentResponseDTO);
    }

    @PutMapping("/{commentReplyId}")
    public ResponseEntity<ApiResponse> update(@PathVariable("commentReplyId") Long commentReplyId, @RequestBody CommentReplyRequestDTO commentReplyRequestDTO) {
        Optional<CommentReply> commentReply = commentReplyService.findById(commentReplyId);
        if (commentReply.isEmpty()) {
            return responseApi(HttpStatus.NOT_FOUND, "Comment not found");
        }
        commentReply.get().setContent(commentReplyRequestDTO.getContent());
        Optional<CommentReply> commentReplyUpdated = commentReplyService.save(commentReply.get());
        if (commentReplyUpdated.isEmpty()) {
            return responseApi(HttpStatus.NOT_FOUND, "Update comment failed");
        }
        CommentReplyResponseDTO commentResponseDTO = ConvertUtils.convert(commentReplyUpdated.get(), CommentReplyResponseDTO.class);
        return responseApi(HttpStatus.NO_CONTENT, String.format("Update comment %s successfully", commentReplyId), commentResponseDTO);
    }

    @DeleteMapping("/{commentReplyId}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("commentReplyId") Long commentReplyId) {
        Optional<CommentReply> commentReply = commentReplyService.findById(commentReplyId);
        if (commentReply.isEmpty()) {
            return responseApi(HttpStatus.NOT_FOUND, "CommentReply not found");
        }
        commentReplyService.delete(commentReply.get());
        return responseApi(HttpStatus.NO_CONTENT, String.format("Delete comment reply %s successfully", commentReplyId));
    }
}
