package com.socialnetwork.socialnetworkjavaspring.controllers;

import com.socialnetwork.socialnetworkjavaspring.DTOs.users.UserRegisterDTO;
import com.socialnetwork.socialnetworkjavaspring.models.Request;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.models.enums.RequestType;
import com.socialnetwork.socialnetworkjavaspring.models.enums.UserStatus;
import com.socialnetwork.socialnetworkjavaspring.services.requests.IRequestService;
import com.socialnetwork.socialnetworkjavaspring.services.users.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class AccountController {

    private static final long REQUEST_TIMEOUT_MILLIS = 60000;

    @Autowired
    private HttpSession session;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRequestService requestService;

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "accounts/create";
    }

    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute UserRegisterDTO userRegisterDTO) {
        ModelAndView modelAndView = new ModelAndView("accounts/create");
        User existingUser = userService.findByEmail(userRegisterDTO.getEmail());

        if (existingUser != null) {
            return handleExistingUser(existingUser, userRegisterDTO, modelAndView);
        }

        return registerNewUser(userRegisterDTO, modelAndView);
    }

    private ModelAndView handleExistingUser(User existingUser, UserRegisterDTO userRegisterDTO,
                                            ModelAndView modelAndView) {
        UserStatus userStatus = existingUser.getStatus();
        switch (userStatus) {
            case ACTIVE:
                String message = "Email is already exist";
                modelAndView.addObject("error", message);
                return modelAndView;
            case INACTIVE:
                return handleInactiveUser(userRegisterDTO, modelAndView);
            default:
                return modelAndView;
        }
    }

    private ModelAndView handleInactiveUser(UserRegisterDTO userRegisterDTO, ModelAndView modelAndView) {
        Optional<Request> request = requestService.findByEmailRequest(userRegisterDTO.getEmail());
        request = request.isPresent()
                ? request
                : Optional.of(new Request(userRegisterDTO.getEmail(), RequestType.ACTIVATE));

        session.setAttribute("email", userRegisterDTO.getEmail());
        session.setAttribute(userRegisterDTO.getEmail(), UserStatus.INACTIVE);

        Optional<Request> savedRequest = requestService.save(request.get());
        if (savedRequest.isEmpty()) {
            return new ModelAndView("errors/404", HttpStatus.NOT_FOUND);
        }

        boolean sendCodeSuccess = requestService.sendCodeToEmail(userRegisterDTO.getEmail(),
                "CONFIRM REGISTER", savedRequest.get().getRequestCode());
        if (!sendCodeSuccess) {
            return new ModelAndView("errors/404", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        modelAndView.setViewName("accounts/activate");
        modelAndView.addObject("title", "Activate account");
        modelAndView.addObject("email", userRegisterDTO.getEmail());
        modelAndView.addObject("urlTarget", "/activate");
        return modelAndView;
    }

    private ModelAndView registerNewUser(UserRegisterDTO userRegisterDTO, ModelAndView modelAndView) {
        Optional<User> registeredUser = userService.registerUser(userRegisterDTO);
        if (registeredUser.isPresent()) {
            return handleInactiveUser(userRegisterDTO, modelAndView);
        }
        return modelAndView;
    }

    @PostMapping("/activate")
    public ModelAndView activate(@RequestParam("code") String code, @RequestParam("email") String email) {
        ModelAndView modelAndView = new ModelAndView("accounts/activate");
        modelAndView.addObject("title", "Activate account");
        UserStatus userStatus = (UserStatus) session.getAttribute(email);
        User user = userService.findByEmail(email);
        Optional<Request> request = requestService.findByEmailRequest(email);

        if (isInvalidActivationRequest(userStatus, user, request)) {
            return new ModelAndView("errors/404", HttpStatus.NOT_FOUND);
        }

        if (!isValidActivationRequest(request.get(), code)) {
            modelAndView.addObject("email", email);
            modelAndView.addObject("message", getErrorMessage(request.get(), code));
            return modelAndView;
        }

        user.setStatus(UserStatus.ACTIVE);
        Optional<User> userUpdate = userService.save(user);
        if (userUpdate.isEmpty()) {
            return new ModelAndView("errors/404", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        requestService.delete(request.get());
        return new ModelAndView("redirect:/login");
    }

    private boolean isInvalidActivationRequest(UserStatus userStatus, User user, Optional<Request> request) {
        return userStatus != UserStatus.INACTIVE || user == null || user.getStatus() == UserStatus.LOCKED ||
                request.isEmpty();
    }

    private boolean isValidActivationRequest(Request request, String code) {
        long timeOut = System.currentTimeMillis() - request.getRequestAt().getTime();
        return request.getRequestType() == RequestType.ACTIVATE && request.getRequestCode().equals(code) && timeOut <=
                REQUEST_TIMEOUT_MILLIS;
    }

    private String getErrorMessage(Request request, String code) {
        if (request.getRequestType() != RequestType.ACTIVATE) {
            return "Request don't exist";
        }
        if (!request.getRequestCode().equals(code)) {
            return "Code is incorrect";
        }
        long timeOut = System.currentTimeMillis() - request.getRequestAt().getTime();
        if (timeOut > REQUEST_TIMEOUT_MILLIS) {
            return "Request is expired! Please submit a new request";
        }
        return "Invalid request";
    }
}
