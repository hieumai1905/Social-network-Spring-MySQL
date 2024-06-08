package com.socialnetwork.socialnetworkjavaspring.models;

import com.socialnetwork.socialnetworkjavaspring.models.enums.PostAccess;
import com.socialnetwork.socialnetworkjavaspring.models.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @Column(name = "post_id", nullable = false, length = 36)
    private String postId;

    @Column(name = "content", nullable = false, length = 2000)
    private String postContent;

    @Column(name = "create_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Transient
    private boolean isLiked;

    @Transient
    private boolean isSaved;

    @Transient
    private PostInteract shareInformation;

    @Transient
    private boolean isHidden;

    @Column(name = "access", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostAccess access;

    @Column(name = "post_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostType postType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Like> likes;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PostInteract> postInteracts;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserTag> userTags;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PostHashtag> postHashtags;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Media> medias;

    public Post(Post post) {
        this.postId = post.getPostId();
        this.postContent = post.getPostContent();
        this.createAt = post.getCreateAt();
        this.isLiked = post.isLiked();
        this.isSaved = post.isSaved();
        this.isHidden = post.isHidden();
        this.access = post.getAccess();
        this.postType = post.getPostType();
        this.user = post.getUser();
        this.comments = post.getComments();
        this.likes = post.getLikes();
        this.postInteracts = post.getPostInteracts();
        this.userTags = post.getUserTags();
        this.postHashtags = post.getPostHashtags();
        this.medias = post.getMedias();
    }

    @PrePersist
    public void prePersist() {
        this.createAt = new Date();
    }
}
