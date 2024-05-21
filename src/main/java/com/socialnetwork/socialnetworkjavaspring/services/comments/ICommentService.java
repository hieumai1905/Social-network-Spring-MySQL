package com.socialnetwork.socialnetworkjavaspring.services.comments;


import com.socialnetwork.socialnetworkjavaspring.models.Comment;
import com.socialnetwork.socialnetworkjavaspring.services.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface ICommentService extends IGeneralService<Comment, Long> {
    Optional<Comment> findById(Long key);

    List<Comment> findAllByPostId(String postId);
}
