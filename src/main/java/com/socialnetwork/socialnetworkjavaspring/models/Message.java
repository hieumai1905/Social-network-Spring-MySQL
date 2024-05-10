package com.socialnetwork.socialnetworkjavaspring.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "messages")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id", nullable = false)
    private Long messageId;

    @Column(name = "content", nullable = false, length = 2000)
    private String content;

    @Column(name = "send_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendAt;

    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @PrePersist
    protected void onCreate() {
        this.sendAt = new Date();
    }
}
