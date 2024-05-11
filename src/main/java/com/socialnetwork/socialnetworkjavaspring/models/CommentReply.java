package com.socialnetwork.socialnetworkjavaspring.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "comment_replies")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_reply_id", nullable = false)
    private Long commentReplyId;

    @Column(name="content", nullable = false)
    private String content;

    @Column(name="reply_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date replyAt;

    @Transient
    private boolean isLiked;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @OneToMany(mappedBy = "commentReply", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Like> likes;

    @PrePersist
    protected void onCreate() {
        this.replyAt = new Date();
    }
}
