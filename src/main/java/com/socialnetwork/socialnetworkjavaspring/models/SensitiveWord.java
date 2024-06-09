package com.socialnetwork.socialnetworkjavaspring.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sensitive_words")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SensitiveWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sensitive_word_id", nullable = false)
    private Long sensitiveWordId;

    @Column(name = "value")
    private String value;
    @Column(name = "create_at")
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    @Column(name = "update_at")
    @Temporal(TemporalType.DATE)
    private Date updatedAt;
    @PrePersist
    public void prePersist() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }
}
