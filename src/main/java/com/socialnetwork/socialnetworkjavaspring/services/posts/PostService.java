package com.socialnetwork.socialnetworkjavaspring.services.posts;

import com.socialnetwork.socialnetworkjavaspring.models.Post;
import com.socialnetwork.socialnetworkjavaspring.repositories.IPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService implements IPostService {
    @Autowired
    private IPostRepository postRepository;

    @Override
    public Optional<Post> save(Post object) {
        return Optional.of(postRepository.save(object));
    }

    @Override
    public Optional<Post> delete(Post object) {
        return Optional.empty();
    }

    @Override
    public List<Post> findAllPostForNewsFeed(String userId) {
        return postRepository.findAllPostForNewsFeed(userId);
    }

    @Override
    public Optional<Post> findById(String key) {
        return postRepository.findById(key);
    }
}
