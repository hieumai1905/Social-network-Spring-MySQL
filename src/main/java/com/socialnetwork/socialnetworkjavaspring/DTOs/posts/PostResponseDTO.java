package com.socialnetwork.socialnetworkjavaspring.DTOs.posts;

import com.socialnetwork.socialnetworkjavaspring.DTOs.comments.CommentResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.medias.MediaResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.models.enums.PostAccess;
import com.socialnetwork.socialnetworkjavaspring.models.enums.PostType;
import com.socialnetwork.socialnetworkjavaspring.models.key.UserTagId;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {
    private String postId;
    private String postContent;
    private Date createAt;
    private PostAccess access;
    private PostType postType;
    private String userId;
    private List<UserTagId> userTags;
    private List<CommentResponseDTO> comments;
    private List<MediaResponseDTO> medias;
}
