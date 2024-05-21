package com.socialnetwork.socialnetworkjavaspring.services.comment_replies;

import com.socialnetwork.socialnetworkjavaspring.models.CommentReply;
import com.socialnetwork.socialnetworkjavaspring.repositories.ICommentReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentReplyService implements ICommentReplyService {
    @Autowired
    private ICommentReplyRepository commentReplyRepository;

    @Override
    public Optional<CommentReply> save(CommentReply object) {
        return Optional.of(commentReplyRepository.save(object));
    }

    @Override
    public Optional<CommentReply> delete(CommentReply object) {
            commentReplyRepository.delete(object);
        return Optional.of(object);
    }

    @Override
    public Optional<CommentReply> findById(Long commentReplyId) {
        return commentReplyRepository.findById(commentReplyId);
    }

    @Override
    public List<CommentReply> findAllByCommentId(Long commentId) {
        return commentReplyRepository.findAllByComment_CommentId(commentId);
    }
}
