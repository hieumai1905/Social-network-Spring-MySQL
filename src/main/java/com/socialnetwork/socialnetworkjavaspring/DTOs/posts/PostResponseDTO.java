package com.socialnetwork.socialnetworkjavaspring.DTOs.posts;

import com.socialnetwork.socialnetworkjavaspring.DTOs.hashtags.HashtagResponse;
import com.socialnetwork.socialnetworkjavaspring.DTOs.users.UserResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.models.enums.PostAccess;
import com.socialnetwork.socialnetworkjavaspring.models.enums.PostType;
import lombok.Data;

import java.util.List;

@Data
public class PostResponseDTO {
    private String postId;
    private String postContent;
    private String createAt;
    private PostAccess access;
    private PostType postType;
    private List<HashtagResponse> hashtags;
    private List<UserResponseDTO> userTags;
    private List<String> medias;
    private UserResponseDTO author;
}
