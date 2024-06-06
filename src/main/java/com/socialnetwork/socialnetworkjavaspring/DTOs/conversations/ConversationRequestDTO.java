package com.socialnetwork.socialnetworkjavaspring.DTOs.conversations;

import com.socialnetwork.socialnetworkjavaspring.DTOs.users.UserResponseDTO;
import lombok.Data;

import java.util.List;

@Data
public class ConversationRequestDTO {
    private String nameConversation;
    private List<String> participantIds;
}
