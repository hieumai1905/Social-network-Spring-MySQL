package com.socialnetwork.socialnetworkjavaspring.DTOs.medias;

import com.socialnetwork.socialnetworkjavaspring.models.enums.MediaType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaResponseDTO {
    private String url;
    private MediaType type;
    private String postId;
}
