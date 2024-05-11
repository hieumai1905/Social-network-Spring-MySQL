package com.socialnetwork.socialnetworkjavaspring.models;

import com.socialnetwork.socialnetworkjavaspring.models.enums.Hobbies;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_hobbies", uniqueConstraints = @UniqueConstraint(columnNames = {"hobbies", "user_id"}))
public class UserHobbies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hobbies_id", nullable = false)
    private Long hobbiesId;

    @Column(name = "hobbies", nullable = false)
    private Hobbies hobbies;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
