package com.socialnetwork.socialnetworkjavaspring.models.key;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UserTagId implements Serializable {
    private String userId;
    private String postId;
}
