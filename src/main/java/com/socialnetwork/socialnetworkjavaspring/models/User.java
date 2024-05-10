package com.socialnetwork.socialnetworkjavaspring.models;

import com.socialnetwork.socialnetworkjavaspring.models.enums.Gender;
import com.socialnetwork.socialnetworkjavaspring.models.enums.RoleUser;
import com.socialnetwork.socialnetworkjavaspring.models.enums.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Transient
    private String confirmPassword;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    private Date dob;

    private String avatar;

    private String coverPhoto;

    @Column(name = "country")
    private String country;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleUser userRole;

    @Column(name = "about_me", length = 1000)
    private String aboutMe;

    @Column(name = "register_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date registerAt;

    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Like> likes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostInteract> postInteracts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CommentReply> commentReplies;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> messages;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Conversation> conversations;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserHobbies> userHobbies;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserTag> userTags;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Album> albums;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Participant> participants;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Relation> relations;

    @PrePersist
    public void prePersist() {
        this.avatar = "/assets/files-upload/images/default-avatar.png";
        this.coverPhoto = "/assets/files-upload/images/default-cover-photo.jpg";
        this.registerAt = new Date();
        this.userRole = RoleUser.ROLE_USER;
        this.status = UserStatus.INACTIVE;
    }
}
