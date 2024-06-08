package com.socialnetwork.socialnetworkjavaspring.controllers;

import com.socialnetwork.socialnetworkjavaspring.DTOs.relations.RelationResponseObjectDTO;
import com.socialnetwork.socialnetworkjavaspring.models.Post;
import com.socialnetwork.socialnetworkjavaspring.models.Relation;
import com.socialnetwork.socialnetworkjavaspring.models.enums.RelationType;
import com.socialnetwork.socialnetworkjavaspring.services.new_feeds.INewsFeedService;
import com.socialnetwork.socialnetworkjavaspring.services.relations.IRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/details/posts")
public class PostController extends ApplicationController {

    @Autowired
    private INewsFeedService newsFeedService;

    @Autowired
    private IRelationService relationService;

    @GetMapping
    public ModelAndView show(@RequestParam("id") String postId) {
        ModelAndView modelAndView = new ModelAndView("index");
        try {
            List<Relation> relations = relationService.findByUserTargetIdAndType(currentUser.getUserId(), RelationType.REQUEST);
            List<RelationResponseObjectDTO> relationDTOs = relationService.findRelationDTOWithMutualFriendCount(currentUser.getUserId(), relations);
            modelAndView.addObject("requestsFriend", relationDTOs);
            List<Post> posts = new ArrayList<>();
            Post postCurrentUser = newsFeedService.findByIdCurrentUser(postId, currentUser.getUserId());
            if (Objects.equals(postCurrentUser.getUser().getUserId(), currentUser.getUserId())) {
                posts.add(postCurrentUser);
                modelAndView.addObject("author", false);
            } else {
                Post postOther = newsFeedService.findById(postId, currentUser.getUserId());
                modelAndView.addObject("author", false);
                posts.add(postOther);
            }
            modelAndView.addObject("posts", posts);
            modelAndView.addObject("page_title", "Post Details");
        } catch (Exception e) {
            return new ModelAndView("errors/404");
        }
        return setAuthor(modelAndView);
    }
}
