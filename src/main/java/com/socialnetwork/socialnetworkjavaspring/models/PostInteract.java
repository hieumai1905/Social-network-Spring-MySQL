package com.socialnetwork.socialnetworkjavaspring.models;

import com.socialnetwork.socialnetworkjavaspring.models.enums.InteractType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "post_interacts")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostInteract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_interact_id", nullable = false)
    private Long postInteractId;

    @Column(name = "content")
    private String content;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private InteractType type;

    @Column(name = "interact_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date interactAt;

    public PostInteract(Post post, User user, InteractType interactType, Date interactAt) {
        this.post = post;
        this.user = user;
        this.type = interactType;
        this.interactAt = interactAt;
    }

    @PrePersist
    protected void onCreate() {
        this.interactAt = new Date();
    }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
