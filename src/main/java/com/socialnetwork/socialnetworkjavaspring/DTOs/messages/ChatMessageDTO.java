package com.socialnetwork.socialnetworkjavaspring.DTOs.messages;

import com.socialnetwork.socialnetworkjavaspring.DTOs.users.UserResponseMessageDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {
    private String content;
    private UserResponseMessageDTO sender;
    private Date sendAt;
    private Long conversationId;
}
