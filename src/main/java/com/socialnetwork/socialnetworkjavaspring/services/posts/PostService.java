package com.socialnetwork.socialnetworkjavaspring.services.posts;

import com.socialnetwork.socialnetworkjavaspring.DTOs.posts.PostRequestDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.posts.PostResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.users.UserResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.models.*;
import com.socialnetwork.socialnetworkjavaspring.models.key.PostHashtagId;
import com.socialnetwork.socialnetworkjavaspring.models.key.UserTagId;
import com.socialnetwork.socialnetworkjavaspring.repositories.IHashtagRepository;
import com.socialnetwork.socialnetworkjavaspring.repositories.IPostRepository;
import com.socialnetwork.socialnetworkjavaspring.repositories.IUserRepository;
import com.socialnetwork.socialnetworkjavaspring.services.medias.IMediaService;
import com.socialnetwork.socialnetworkjavaspring.utils.ConvertUtils;
import com.socialnetwork.socialnetworkjavaspring.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class PostService implements IPostService {
    @Autowired
    private IPostRepository postRepository;
    @Autowired
    private IHashtagRepository hashtagRepository;
    @Autowired
    private IMediaService mediaService;
    @Autowired
    private IUserRepository userRepository;

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
    public PostResponseDTO save(PostRequestDTO request, List<MultipartFile> files, User user) {
        Post post = new Post();
        post.setPostContent(request.getContent());
        post.setPostType(request.getPostType());
        post.setAccess(request.getAccess());
        post.setPostId(UUID.randomUUID().toString());
        post.setUser(user);
        List<UserResponseDTO> userTags = setUserTags(post, request.getUserTagIds());
        setPostHagTags(post, request.getHagTags());
        postRepository.save(post);

        List<String> medias = mediaService.saveList(files, post.getPostId(), user.getUserId());

        PostResponseDTO postResponseDTO = ConvertUtils.convert(post, PostResponseDTO.class);
        postResponseDTO.setCreateAt(DateTimeUtils.dateToString(new Date(), DateTimeUtils.FORMAT_DATE_TIME5,
                DateTimeUtils.TIMEZONE_VN));
        postResponseDTO.setMedias(medias);
        postResponseDTO.setUserTags(userTags);
        postResponseDTO.setAuthor(ConvertUtils.convert(user, UserResponseDTO.class));

        return postResponseDTO;
    }

    private List<UserResponseDTO> setUserTags(Post post, List<String> userTagIds) {
        List<UserTag> userTags = new ArrayList<>();
        List<UserResponseDTO> userResponseDTOS = new ArrayList<>();

        if(userTagIds == null)
            return userResponseDTOS;

        for (String userId : userTagIds) {
            User user = userRepository.findById(userId).orElseThrow(
                    () -> new NullPointerException("User not found!")
            );
            UserTagId userTagId = new UserTagId(userId, post.getPostId());
            UserTag userTag = new UserTag(userTagId, user, post);
            userTags.add(userTag);
            userResponseDTOS.add(ConvertUtils.convert(user, UserResponseDTO.class));
        }
        post.setUserTags(userTags);

        return userResponseDTOS;
    }

    private void setPostHagTags(Post post, List<String> hashTags) {
        post.setPostHashtags(new ArrayList<>());
        if(hashTags == null)
            return;

        for (String hashtag : hashTags) {
            Hashtag existHashtag = hashtagRepository.findByHashtag(hashtag)
                    .orElse(null);
            if(existHashtag == null){
                existHashtag = new Hashtag(hashtag);
                hashtagRepository.save(existHashtag);
            }
            PostHashtag postHashtag = new PostHashtag(new PostHashtagId(
                    post.getPostId(), existHashtag.getHashtagId()
            ), post, existHashtag);
            post.getPostHashtags().add(postHashtag);
        }
    }

    @Override
    public Optional<Post> findById(String postId) {
        return postRepository.findById(postId);
    }

    @Override
    public String delete(String postId, String userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NullPointerException("Post not found!"));
        if(!post.getUser().getUserId().equals(userId))
            throw new RuntimeException("This post is not your!");
        postRepository.delete(post);
        return postId;
    }
}
