package com.socialnetwork.socialnetworkjavaspring.services.comment_replies;


import com.socialnetwork.socialnetworkjavaspring.models.CommentReply;
import com.socialnetwork.socialnetworkjavaspring.services.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface ICommentReplyService extends IGeneralService<CommentReply, Long> {
    Optional<CommentReply> findById(Long commentReplyId);

    List<CommentReply> findAllByCommentId(Long commentId);
}
