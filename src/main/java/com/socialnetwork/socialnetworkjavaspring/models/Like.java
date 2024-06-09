package com.socialnetwork.socialnetworkjavaspring.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "post_id"}),
        @UniqueConstraint(columnNames = {"user_id", "comment_id"}),
        @UniqueConstraint(columnNames = {"user_id", "comment_reply_id"})
})
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id", nullable = false)
    private Long likeId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "comment_reply_id")
    private CommentReply commentReply;

    public Like(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    public Like(User user, Comment comment) {
        this.user = user;
        this.comment = comment;
    }

    public Like(User user, CommentReply commentReply) {
        this.user = user;
        this.commentReply = commentReply;
    }
}
