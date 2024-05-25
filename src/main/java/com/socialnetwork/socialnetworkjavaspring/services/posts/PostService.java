package com.socialnetwork.socialnetworkjavaspring.services.posts;

import com.socialnetwork.socialnetworkjavaspring.DTOs.posts.PostRequestDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.posts.PostResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.users.UserResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.models.*;
import com.socialnetwork.socialnetworkjavaspring.models.enums.InteractType;
import com.socialnetwork.socialnetworkjavaspring.models.enums.PostType;
import com.socialnetwork.socialnetworkjavaspring.models.key.PostHashtagId;
import com.socialnetwork.socialnetworkjavaspring.models.key.UserTagId;
import com.socialnetwork.socialnetworkjavaspring.repositories.IHashtagRepository;
import com.socialnetwork.socialnetworkjavaspring.repositories.IPostRepository;
import com.socialnetwork.socialnetworkjavaspring.repositories.IUserRepository;
import com.socialnetwork.socialnetworkjavaspring.services.likes.ILikeService;
import com.socialnetwork.socialnetworkjavaspring.services.medias.IMediaService;
import com.socialnetwork.socialnetworkjavaspring.services.post_interacts.IPostInteractService;
import com.socialnetwork.socialnetworkjavaspring.services.sessions.SessionService;
import com.socialnetwork.socialnetworkjavaspring.utils.ConvertUtils;
import com.socialnetwork.socialnetworkjavaspring.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class PostService extends PostGeneralService implements IPostService {
    @Autowired
    private IPostRepository postRepository;
    @Autowired
    private IHashtagRepository hashtagRepository;
    @Autowired
    private IMediaService mediaService;
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ILikeService likeService;

    @Autowired
    private IPostInteractService postInteractService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private SessionService sessionService;

    @Override
    public List<Post> findAllPostForNewsFeed(String userId) {
        return postRepository.findAllPostForNewsFeed(userId);
    }

    @Override
    public List<Post> findAllPostForProfileMe(String userId) {
        return postRepository.findAllByUserCurrent(userId);
    }

    @Override
    public List<Post> findAllPostForProfileOther(String userId, Boolean isFriend) {
        if (isFriend)
            return postRepository.findAllByUserFriend(userId);

        return postRepository.findAllPostForNewsFeed(userId);
    }

    @Override
    public PostResponseDTO save(String postId, PostRequestDTO request, List<MultipartFile> files, User user) {
        Post post = null;
        List<Comment> comments = new ArrayList<>();
        List<Like> likes = new ArrayList<>();
        List<PostInteract> postInteracts = new ArrayList<>();
        if(postId != null) {
            post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found!"));
            comments = post.getComments();
            likes = post.getLikes();
            postInteracts = post.getPostInteracts();
            postRepository.delete(post);
        }
        post = new Post();
        post.setPostId(postId == null ? UUID.randomUUID().toString() : postId);
        post.setPostContent(request.getContent());
        post.setPostType(request.getPostType());
        post.setAccess(request.getAccess());
        post.setUser(user);
        post.setPostInteracts(postInteracts);
        post.setComments(comments);
        post.setLikes(likes);
        List<UserResponseDTO> userTags = setUserTags(post, request.getUserTagIds());
        setPostHagTags(post, request.getHagTags());
        postRepository.save(post);

        List<String> medias = mediaService.saveList(files, post.getPostId(), user.getUserId());
        changeUserPhoto(request, user, medias);
        PostResponseDTO postResponseDTO = ConvertUtils.convert(post, PostResponseDTO.class);
        postResponseDTO.setCreateAt(DateTimeUtils.dateToString(new Date(), DateTimeUtils.FORMAT_DATE_TIME5,
                DateTimeUtils.TIMEZONE_VN));
        postResponseDTO.setMedias(medias);
        postResponseDTO.setUserTags(userTags);
        postResponseDTO.setAuthor(ConvertUtils.convert(user, UserResponseDTO.class));

        return postResponseDTO;
    }

    private void changeUserPhoto(PostRequestDTO request, User user, List<String> medias) {
        if(request.getPostType() != null && !request.getPostType().equals(PostType.POST)){
            User existingUser = userRepository.findById(user.getUserId()).orElseThrow(() -> new NullPointerException("User not found!"));
            if(request.getPostType().equals(PostType.CHANGE_AVATAR)) {
                existingUser.setAvatar(medias.get(0));
                user.setAvatar(medias.get(0));
            }
            else if(request.getPostType().equals(PostType.CHANGE_COVER)) {
                existingUser.setCoverPhoto(medias.get(0));
                user.setCoverPhoto(medias.get(0));
            }
            userRepository.save(existingUser);
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            existingUser.getEmail(),
                            existingUser.getPassword()
                    )
            );
            sessionService.login(authentication);
        }
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
    public PostResponseDTO findPostResponseById(String postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NullPointerException("Post not found!"));
        PostResponseDTO postResponseDTO = ConvertUtils.convert(post, PostResponseDTO.class);
        postResponseDTO.setAuthor(ConvertUtils.convert(post.getUser(), UserResponseDTO.class));
        if(post.getMedias() != null){
            postResponseDTO.setMedias(new ArrayList<>());
            for (Media media : post.getMedias()) {
                postResponseDTO.getMedias().add(media.getUrl());
            }
        }
        if(post.getUserTags() != null){
            postResponseDTO.setUserTags(new ArrayList<>());
            for (UserTag userTag : post.getUserTags()) {
                postResponseDTO.getUserTags().add(new UserResponseDTO(
                        userTag.getUser().getUserId(),
                        userTag.getUser().getFullName(),
                        userTag.getUser().getAvatar(),
                        null
                ));
            }
        }
        return postResponseDTO;
    }

    @Override
    public String delete(String postId, String userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NullPointerException("Post not found!"));
        if(!post.getUser().getUserId().equals(userId))
            throw new RuntimeException("This post is not your!");
        postRepository.delete(post);
        return postId;
    }

    @Override
    public List<Post> findPostByInteractType(InteractType interactType, String userId) {
        List<Post> posts = postRepository.findPostByInteractType(String.valueOf(interactType), userId);
        posts.forEach(post -> {
            boolean isLiked = likeService.existsByPostIdAndUserId(post.getPostId(), userId);
            PostInteract savedPostInteract = postInteractService.checkExistPostInteract(post, InteractType.SAVED, userId);
            PostInteract hiddenPostInteract = postInteractService.checkExistPostInteract(post, InteractType.HIDDEN, userId);
            post.setLiked(isLiked);
            post.setSaved(savedPostInteract != null);
            post.setHidden(hiddenPostInteract != null);
            setLikedStatusForCommentsAndReplies(post, userId);
            sortCommentsAndReplies(post);
        });
        return posts;
    }
}
