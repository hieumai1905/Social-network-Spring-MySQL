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
public class ParticipantId implements Serializable {
    private String userId;
    private Long conversationId;
}
