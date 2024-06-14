package com.socialnetwork.socialnetworkjavaspring.DTOs.conversations;

import com.socialnetwork.socialnetworkjavaspring.DTOs.users.UserResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.models.enums.ConversationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

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
    private List<UserResponseDTO> members;
}
