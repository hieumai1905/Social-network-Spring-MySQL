package com.socialnetwork.socialnetworkjavaspring.configs.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String senderId = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("senderId");
        String conversationId = (String) headerAccessor.getSessionAttributes().get("conversationId");
        if (senderId != null) {
            log.info("UserId: {} LEAVE ConversationId: {}", senderId, conversationId);
        }
    }
}
