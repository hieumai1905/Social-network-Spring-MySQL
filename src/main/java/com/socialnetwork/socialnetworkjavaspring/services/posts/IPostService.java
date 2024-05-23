package com.socialnetwork.socialnetworkjavaspring.services.posts;

import com.socialnetwork.socialnetworkjavaspring.DTOs.posts.PostRequestDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.posts.PostResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.models.Post;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.services.IGeneralService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IPostService extends IGeneralService<Post, String> {
    List<Post> findAllPostForNewsFeed(String userId);
    PostResponseDTO save(PostRequestDTO request, List<MultipartFile> files, User user);

    Optional<Post> findById(String postId);

    String delete(String postId, String userId);
}
