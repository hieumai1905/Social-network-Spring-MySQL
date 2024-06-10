package com.socialnetwork.socialnetworkjavaspring.controllers;

import com.socialnetwork.socialnetworkjavaspring.DTOs.relations.RelationResponseObjectDTO;
import com.socialnetwork.socialnetworkjavaspring.models.Relation;
import com.socialnetwork.socialnetworkjavaspring.models.enums.RelationType;
import com.socialnetwork.socialnetworkjavaspring.services.relations.IRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/request-friend")
public class UserRequestFriendController extends ApplicationController {

    @Autowired
    private IRelationService relationService;

    @GetMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/users/requests");
        List<Relation> relations = relationService.findByUserTargetIdAndType(currentUser.getUserId(), RelationType.REQUEST);
        List<RelationResponseObjectDTO> relationDTOs = relationService.findRelationDTOWithMutualFriendCount(currentUser.getUserId(), relations);
        modelAndView.addObject("requestsFriend", relationDTOs);
        return setAuthor(modelAndView);
    }
}
