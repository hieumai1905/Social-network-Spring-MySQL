package com.socialnetwork.socialnetworkjavaspring.services.like;


import com.socialnetwork.socialnetworkjavaspring.models.Like;
import com.socialnetwork.socialnetworkjavaspring.services.IGeneralService;

public interface ILikeService extends IGeneralService<Like, Long> {

    boolean existsByPostIdAndUserId(String postId, String userId);

    boolean existsByCommentIdAndUserId(Long commentId, String userId);

    boolean existsByCommentReplyIdAndUserId(Long commentReplyId, String userId);
}
