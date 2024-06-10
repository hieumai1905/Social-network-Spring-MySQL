 package com.socialnetwork.socialnetworkjavaspring.DTOs.comments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO {
    private Long commentId;
    private String content;
    private String commentAt;
    private String userId;
    private String postId;
    private String updateAt;
    private List<CommentReplyResponseDTO> commentReplies;
}
