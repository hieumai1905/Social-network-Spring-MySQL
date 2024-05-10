package com.socialnetwork.socialnetworkjavaspring.models.key;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PostHashtagId implements Serializable {
    private String postId;
    private Long hashtagId;
}
