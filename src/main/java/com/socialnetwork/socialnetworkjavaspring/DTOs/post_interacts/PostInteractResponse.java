package com.socialnetwork.socialnetworkjavaspring.DTOs.post_interacts;

import com.socialnetwork.socialnetworkjavaspring.models.enums.InteractType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostInteractResponse {
    private Long postInteractId;

    private String content;

    private InteractType type;

    private Date interactAt;
}
