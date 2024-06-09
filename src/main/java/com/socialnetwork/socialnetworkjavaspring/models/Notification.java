package com.socialnetwork.socialnetworkjavaspring.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "notifications")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id", nullable = false)
    private Long notificationId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "seen", nullable = false)
    private Boolean seen;

    private String image;

    @Column(name = "url_redirect", nullable = false)
    private String urlRedirect;

    @Column(name = "notification_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date notificationAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "user_target_id", nullable = false)
    private User userTarget;

    @PrePersist
    public void prePersist() {
        this.notificationAt = new Date();
    }
}
