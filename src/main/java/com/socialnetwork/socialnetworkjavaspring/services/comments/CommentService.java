package com.socialnetwork.socialnetworkjavaspring.services.comments;

import com.socialnetwork.socialnetworkjavaspring.models.Comment;
import com.socialnetwork.socialnetworkjavaspring.repositories.ICommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService implements ICommentService {
    @Autowired
    private ICommentRepository commentRepository;

    @Override
    public Optional<Comment> save(Comment object) {
        return Optional.of(commentRepository.save(object));
    }

    @Override
    public Optional<Comment> delete(Comment object) {
        commentRepository.delete(object);
        return Optional.of(object);
    }

    @Override
    public Optional<Comment> findById(Long key) {
        return commentRepository.findById(key);
    }

    @Override
    public List<Comment> findAllByPostId(String postId) {
        return commentRepository.findAllByPost_PostId(postId);
    }
}
