package com.socialnetwork.socialnetworkjavaspring.controllers;

import com.socialnetwork.socialnetworkjavaspring.DTOs.people.SearchPeopleRequestDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.people.SearchPeopleResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.posts.SearchPostRequestDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.posts.SearchPostResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.models.Post;
import com.socialnetwork.socialnetworkjavaspring.services.posts.IPostService;
import com.socialnetwork.socialnetworkjavaspring.services.users.IUserService;
import com.socialnetwork.socialnetworkjavaspring.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@Controller
@RequestMapping("/search")
public class SearchController extends ApplicationController{
    @Autowired
    private IUserService userService;
    @Autowired
    private IPostService postService;
    @GetMapping
    public ModelAndView index(@RequestParam(required = false) String q, @RequestParam("type") String type) {
        if(type.equals(Constants.SEARCH_TYPE_PEOPLE))
            return handleSearchPeople(q);
        else if(type.equals(Constants.SEARCH_TYPE_POST))
            return handleSearchPosts(q);
        return new ModelAndView("/errors/404");
    }

    private ModelAndView handleSearchPosts(String q) {
        ModelAndView modelAndView = new ModelAndView("people/index");
        Page<Post> posts = postService.findByContentAndHashtags(setSearchPostRequest(q), currentUser);
        modelAndView.addObject("posts", posts.getContent());
        modelAndView.addObject("search_value", q);
        modelAndView.addObject("search_type", Constants.SEARCH_TYPE_POST);
        return setAuthor(modelAndView);
    }

    private SearchPostRequestDTO setSearchPostRequest(String inputText) {
        String[] words = inputText.trim().split("\\s+");
        SearchPostRequestDTO searchPostRequestDTO = new SearchPostRequestDTO();
        searchPostRequestDTO.setHashtags(new ArrayList<>());
        StringBuilder contentBuilder = new StringBuilder();
        for (String word : words) {
            if (word.startsWith("#")) {
                searchPostRequestDTO.getHashtags().add(word);
            } else {
                contentBuilder.append(word).append(" ");
            }
        }
        String content = contentBuilder.toString().trim();
        searchPostRequestDTO.setContent(content);
        return searchPostRequestDTO;
    }


    private ModelAndView handleSearchPeople(String q) {
        ModelAndView modelAndView = new ModelAndView("people/index");
        SearchPeopleResponseDTO searchPeopleResponseDTO = userService
                .findByFullNameLikeIgnoreCaseAndAccents(new SearchPeopleRequestDTO(q), currentUser.getUserId());
        modelAndView.addObject("users", searchPeopleResponseDTO.getUserResponses());
        modelAndView.addObject("search_value", q);
        modelAndView.addObject("search_type", Constants.SEARCH_TYPE_PEOPLE);
        return setAuthor(modelAndView);
    }
}
