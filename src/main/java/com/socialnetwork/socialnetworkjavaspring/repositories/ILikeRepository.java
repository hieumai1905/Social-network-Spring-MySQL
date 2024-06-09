package com.socialnetwork.socialnetworkjavaspring.repositories;


import com.socialnetwork.socialnetworkjavaspring.models.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ILikeRepository extends JpaRepository<Like, Long> {

    boolean existsByPost_PostIdAndUser_UserId(String postId, String userId);

    boolean existsByComment_CommentIdAndUser_UserId(Long commentId, String userId);

    boolean existsByCommentReply_CommentReplyIdAndUser_UserId(Long commentReplyId, String userId);

    Optional<Like> findByPost_PostIdAndUser_UserId(String postId, String userId);

    Optional<Like> findByComment_CommentIdAndUser_UserId(Long commentId, String userId);

    Optional<Like> findByCommentReply_CommentReplyIdAndUser_UserId(Long commentReplyId, String userId);
}
