package com.socialnetwork.socialnetworkjavaspring.DTOs.comments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentReplyResponseDTO {
    private Long commentReplyId;
    private String content;
    private String replyAt;
    private String userId;
}
