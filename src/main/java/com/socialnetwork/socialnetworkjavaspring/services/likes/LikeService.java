package com.socialnetwork.socialnetworkjavaspring.services.likes;

import com.socialnetwork.socialnetworkjavaspring.models.Like;
import com.socialnetwork.socialnetworkjavaspring.repositories.ILikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService implements ILikeService {
    @Autowired
    private ILikeRepository likeRepository;

    @Override
    public Optional<Like> save(Like object) {
        return Optional.of(likeRepository.save(object));
    }

    @Override
    public Optional<Like> delete(Like object) {
        likeRepository.delete(object);
        return Optional.of(object);
    }

    @Override
    public boolean existsByPostIdAndUserId(String postId, String userId) {
        return likeRepository.existsByPost_PostIdAndUser_UserId(postId, userId);
    }

    @Override
    public boolean existsByCommentIdAndUserId(Long commentId, String userId) {
        return likeRepository.existsByComment_CommentIdAndUser_UserId(commentId, userId);
    }

    @Override
    public boolean existsByCommentReplyIdAndUserId(Long commentReplyId, String userId) {
        return likeRepository.existsByCommentReply_CommentReplyIdAndUser_UserId(commentReplyId, userId);
    }

    @Override
    public Optional<Like> findByPostIdAndUserId(String postId, String userId) {
        return likeRepository.findByPost_PostIdAndUser_UserId(postId, userId);
    }

    @Override
    public Optional<Like> findByCommentIdAndUserId(Long commentId, String userId) {
        return likeRepository.findByComment_CommentIdAndUser_UserId(commentId, userId);
    }

    @Override
    public Optional<Like> findByCommentReplyIdAndUserId(Long commentReplyId, String userId) {
        return likeRepository.findByCommentReply_CommentReplyIdAndUser_UserId(commentReplyId, userId);
    }
}
