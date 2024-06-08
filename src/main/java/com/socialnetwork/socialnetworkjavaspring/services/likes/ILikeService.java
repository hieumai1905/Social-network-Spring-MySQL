package com.socialnetwork.socialnetworkjavaspring.services.likes;


import com.socialnetwork.socialnetworkjavaspring.models.Like;
import com.socialnetwork.socialnetworkjavaspring.services.IGeneralService;

import java.util.Optional;

public interface ILikeService extends IGeneralService<Like, Long> {

    boolean existsByPostIdAndUserId(String postId, String userId);

    boolean existsByCommentIdAndUserId(Long commentId, String userId);

    boolean existsByCommentReplyIdAndUserId(Long commentReplyId, String userId);

    Optional<Like> findByPostIdAndUserId(String postId, String userId);

    Optional<Like> findByCommentIdAndUserId(Long commentId, String userId);

    Optional<Like> findByCommentReplyIdAndUserId(Long commentReplyId, String userId);
}
