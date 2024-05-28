package com.socialnetwork.socialnetworkjavaspring.controllers;

import com.socialnetwork.socialnetworkjavaspring.models.enums.InteractType;
import com.socialnetwork.socialnetworkjavaspring.repositories.IPostInteractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/reports")
public class ReportsController extends ApplicationController{

    @Autowired
    private IPostInteractRepository postInteractRepository;
    @GetMapping(value = {"", "/index"})
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("admin/reports");
        modelAndView.addObject("reports", postInteractRepository.findAllByType(InteractType.REPORT));
        return setAuthor(modelAndView);
    }
}
