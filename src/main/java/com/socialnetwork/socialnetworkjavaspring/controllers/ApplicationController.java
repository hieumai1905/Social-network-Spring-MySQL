package com.socialnetwork.socialnetworkjavaspring.controllers;

import com.socialnetwork.socialnetworkjavaspring.DTOs.relations.RelationResponseObjectDTO;
import com.socialnetwork.socialnetworkjavaspring.models.*;
import com.socialnetwork.socialnetworkjavaspring.models.enums.RelationType;
import com.socialnetwork.socialnetworkjavaspring.models.enums.RoleUser;
import com.socialnetwork.socialnetworkjavaspring.services.new_feeds.NewsFeedService;
import com.socialnetwork.socialnetworkjavaspring.services.notifications.INotificationService;
import com.socialnetwork.socialnetworkjavaspring.services.post_interacts.IPostInteractService;
import com.socialnetwork.socialnetworkjavaspring.services.relations.IRelationService;
import com.socialnetwork.socialnetworkjavaspring.services.sessions.SessionService;
import com.socialnetwork.socialnetworkjavaspring.utils.ConvertUtils;
import com.socialnetwork.socialnetworkjavaspring.utils.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
public class ApplicationController {
    protected User currentUser;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private NewsFeedService newsFeedService;

    @Autowired
    private IRelationService relationService;

    @Autowired
    private IPostInteractService postInteractService;

    @Autowired
    private INotificationService noticiationService;

    @ModelAttribute
    public void getCurrentUser() {
        this.currentUser = sessionService.currentUser();
    }

    @GetMapping(value = {"/", "/index"})
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        if (currentUser.getUserRole().equals(RoleUser.ROLE_ADMIN)) {
            return setAuthor(new ModelAndView("admin/index"));
        }
        List<Post> posts = newsFeedService.getNewsFeed(currentUser.getUserId());
        List<PostInteract> postInteracts = postInteractService.findAllPostInteractShareByUserId(currentUser.getUserId());
        mergeListPostShareAndSort(posts, postInteracts);
        modelAndView.addObject("posts", posts);
        List<Relation> relations = relationService.findByUserTargetIdAndType(currentUser.getUserId(), RelationType.REQUEST);
        List<RelationResponseObjectDTO> relationDTOs = relationService.findRelationDTOWithMutualFriendCount(currentUser.getUserId(), relations);
        modelAndView.addObject("requestsFriend", relationDTOs);
        modelAndView.addObject("page_title", "News Feed");
        return setAuthor(modelAndView);
    }

    private void mergeListPostShareAndSort(List<Post> posts, List<PostInteract> postInteracts) {
        for (PostInteract postInteract : postInteracts) {
            Post postNew = new Post(postInteract.getPost());
            postNew.setShareInformation(postInteract);
            posts.add(postNew);
        }
        posts.sort((o1, o2) -> {
            Date o1SortValue = o1.getShareInformation() != null ? o1.getShareInformation().getInteractAt() : o1.getCreateAt();
            Date o2SortValue = o2.getShareInformation() != null ? o2.getShareInformation().getInteractAt() : o2.getCreateAt();
            return o2SortValue.compareTo(o1SortValue);
        });
    }

    protected ModelAndView setAuthor(ModelAndView modelAndView) {
        modelAndView.addObject("currentUser", currentUser);
        List<Notification> notifications = noticiationService.findAllByUserId(currentUser.getUserId());
        modelAndView.addObject("notifications", notifications);
        return modelAndView;
    }

    public ResponseEntity<ApiResponse> responseApi(HttpStatus code, String message) {
        return ResponseEntity.ok(new ApiResponse(code.value(), message, null));
    }

    public ResponseEntity<ApiResponse> responseApi(HttpStatus code, String message, Object data) {
        return ResponseEntity.ok(new ApiResponse(code.value(), message, data));
    }
}
