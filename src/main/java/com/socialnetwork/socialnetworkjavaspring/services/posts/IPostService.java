package com.socialnetwork.socialnetworkjavaspring.services.posts;

import com.socialnetwork.socialnetworkjavaspring.DTOs.posts.PostRequestDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.posts.PostResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.posts.SearchPostRequestDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.posts.SearchPostResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.models.Post;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.models.enums.InteractType;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IPostService{
    List<Post> findAllPostForNewsFeed(String userId);

    PostResponseDTO save(String postId, PostRequestDTO request, List<MultipartFile> files, User user);

    Optional<Post> findById(String postId);
    PostResponseDTO findPostResponseById(String postId);

    List<Post> findPostByInteractType(InteractType interactType, String userId);

    String delete(String postId, User user);

    List<Post> findAllPostForProfileOther(String userId, Boolean isFriend);

    List<Post> findAllPostForProfileMe(String userId);

    Page<Post> findByContentAndHashtags(SearchPostRequestDTO request, User user);

    SearchPostResponseDTO convertPostsToSearchPostResponseDTO(Page<Post> posts, SearchPostRequestDTO request);

    Boolean checkValidContent(String content);
}
