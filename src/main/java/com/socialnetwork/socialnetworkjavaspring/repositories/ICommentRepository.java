package com.socialnetwork.socialnetworkjavaspring.repositories;

import com.socialnetwork.socialnetworkjavaspring.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost_PostId(String postId);
}
