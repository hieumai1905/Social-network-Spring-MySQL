package com.socialnetwork.socialnetworkjavaspring.DTOs.conversations;

import com.socialnetwork.socialnetworkjavaspring.models.enums.ConversationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationResponseDTO {
    private Long conversationId;
    private String name;
    private Date createdAt;
    private ConversationType type;
    private String avatar;
    private String userId;
}
