package com.socialnetwork.socialnetworkjavaspring.DTOs.comments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentReplyRequestDTO {
    private String content;
    private Long commentId;
}
