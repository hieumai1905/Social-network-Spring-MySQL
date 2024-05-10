package com.socialnetwork.socialnetworkjavaspring.models;

import com.socialnetwork.socialnetworkjavaspring.models.key.PostHashtagId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "post_hashtags")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostHashtag {
    @EmbeddedId
    private PostHashtagId postHashtagId;

    @ManyToOne
    @MapsId("postId")
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @MapsId("hashtagId")
    @JoinColumn(name = "hashtag_id", nullable = false)
    private Hashtag hashtag;
}
