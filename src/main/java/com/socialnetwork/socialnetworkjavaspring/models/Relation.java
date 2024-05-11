package com.socialnetwork.socialnetworkjavaspring.models;

import com.socialnetwork.socialnetworkjavaspring.models.enums.RelationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "relations")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Relation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relation_id", nullable = false)
    private Long relationId;

    @Column(name = "type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private RelationType type;

    @Column(name = "set_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date setAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "user_target_id")
    private User userTarget;

    @PrePersist
    public void prePersist() {
        this.setAt = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        this.setAt = new Date();
    }
}
