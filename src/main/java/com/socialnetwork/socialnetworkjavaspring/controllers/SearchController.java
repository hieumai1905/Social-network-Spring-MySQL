package com.socialnetwork.socialnetworkjavaspring.controllers;

import com.socialnetwork.socialnetworkjavaspring.DTOs.people.SearchPeopleRequestDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.people.SearchPeopleResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.services.users.IUserService;
import com.socialnetwork.socialnetworkjavaspring.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/search")
public class SearchController extends ApplicationController{
    @Autowired
    private IUserService userService;
    @GetMapping
    public ModelAndView index(@RequestParam(required = false) String q, @RequestParam("type") String type) {
        if(type.equals(Constants.SEARCH_TYPE_PEOPLE))
            return handleSearchPeople(q);
        else if(type.equals(Constants.SEARCH_TYPE_POST))
            return handleSearchPosts();
        return new ModelAndView("/errors/404");
    }

    private ModelAndView handleSearchPosts() {
        return null;
    }

    private ModelAndView handleSearchPeople(String q) {
        ModelAndView modelAndView = new ModelAndView("people/index");
        SearchPeopleResponseDTO searchPeopleResponseDTO = userService
                .findByFullNameLikeIgnoreCaseAndAccents(new SearchPeopleRequestDTO(q), currentUser.getUserId());
        modelAndView.addObject("users", searchPeopleResponseDTO.getUserResponses());
        modelAndView.addObject("search_value", q);
        return setAuthor(modelAndView);
    }
}
