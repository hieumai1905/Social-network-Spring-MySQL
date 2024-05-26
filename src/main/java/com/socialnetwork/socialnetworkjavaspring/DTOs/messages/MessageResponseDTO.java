package com.socialnetwork.socialnetworkjavaspring.DTOs.messages;

import com.socialnetwork.socialnetworkjavaspring.DTOs.users.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDTO {
    private Long messageId;
    private String content;
    private Date sendAt;
    private Long conversationId;
    private UserResponseDTO userSender;
    private boolean isUserCurrent;
}
