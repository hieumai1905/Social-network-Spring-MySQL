package com.socialnetwork.socialnetworkjavaspring.controllers.errors;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorsController {
    @GetMapping("/no-access")
    public String noAccess() {
        return "errors/no-access";
    }
}
