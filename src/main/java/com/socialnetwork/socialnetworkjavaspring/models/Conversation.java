package com.socialnetwork.socialnetworkjavaspring.models;

import com.socialnetwork.socialnetworkjavaspring.models.enums.ConversationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "conversations")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversation_id", nullable = false)
    private Long conversationId;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ConversationType type;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "avatar", length = 100)
    private String avatar;

    @ManyToOne
    @JoinColumn(name = "manage_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "conversation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Message> messages;

    @OneToMany(mappedBy = "conversation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Participant> participants;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
        this.avatar = null;
    }
}
