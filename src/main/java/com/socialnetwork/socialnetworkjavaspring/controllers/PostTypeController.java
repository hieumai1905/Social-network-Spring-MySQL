package com.socialnetwork.socialnetworkjavaspring.controllers;

import com.socialnetwork.socialnetworkjavaspring.models.Post;
import com.socialnetwork.socialnetworkjavaspring.models.enums.InteractType;
import com.socialnetwork.socialnetworkjavaspring.services.new_feeds.INewsFeedService;
import com.socialnetwork.socialnetworkjavaspring.services.posts.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostTypeController extends ApplicationController {

    @Autowired
    private IPostService postService;

    @GetMapping("/{type}")
    public ModelAndView getAllPostByType(@PathVariable String type) {
        ModelAndView modelAndView = new ModelAndView("posts/post_interact");
        InteractType interactType;
        try {
            interactType = InteractType.valueOf(type.toUpperCase());
        } catch (Exception ex) {
            modelAndView.setViewName("errors/404");
            return modelAndView;
        }
        List<Post> postInteractType = postService.findPostByInteractType(interactType, currentUser.getUserId());
        modelAndView.addObject("interact_type", "POSTS " + interactType);
        modelAndView.addObject("posts", postInteractType);
        return setAuthor(modelAndView);
    }
}
