package com.socialnetwork.socialnetworkjavaspring.DTOs.messages;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequestDTO {
    private String senderId;
    private String content;
    private Long conversationId;
    private String messageType;
    private String userTargetId;
    private String typeNotification;
    private String dataId;
}
