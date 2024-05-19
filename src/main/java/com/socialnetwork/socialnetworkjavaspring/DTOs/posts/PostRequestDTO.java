package com.socialnetwork.socialnetworkjavaspring.DTOs.posts;

import com.socialnetwork.socialnetworkjavaspring.models.enums.PostAccess;
import com.socialnetwork.socialnetworkjavaspring.models.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDTO {
    private String postContent;
    private PostAccess access;
    private PostType postType;
    private List<String> userTagIds;
}
