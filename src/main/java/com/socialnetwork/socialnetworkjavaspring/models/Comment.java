package com.socialnetwork.socialnetworkjavaspring.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "comments")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Long commentId;

    @Column(name = "comment_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date commentAt;

    @Column(name = "content", nullable = false)
    private String content;

    @Transient
    private boolean isLiked;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<CommentReply> commentReplies;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<Like> likes;

    @PrePersist
    public void onCreate() {
        this.commentAt = new Date();
    }

    @PreUpdate
    public void onUpdate() {
        this.commentAt = new Date();
    }
}
