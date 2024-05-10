package com.socialnetwork.socialnetworkjavaspring.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "hash_tags")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hashtagId;

    @Column(nullable = false, unique = true)
    private String hashtag;

    @OneToMany(mappedBy = "hashtag", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostHashtag> postHashtags;
}
