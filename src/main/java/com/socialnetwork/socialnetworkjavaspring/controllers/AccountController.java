package com.socialnetwork.socialnetworkjavaspring.controllers;

import com.socialnetwork.socialnetworkjavaspring.DTOs.users.UserPasswordUpdateDTO;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "accounts/forgot-password";
    }

    @PostMapping("/forgot-password")
    public ModelAndView getCode(@RequestParam("email") String email) {
        ModelAndView modelAndView = new ModelAndView("accounts/forgot-password");
        User userExist = userService.findByEmail(email);
        if (userExist == null) {
            modelAndView.addObject("error", "Email is not exist");
            return modelAndView;
        }
        if (userExist.getStatus() == UserStatus.LOCKED) {
            modelAndView.addObject("error", "Account is locked");
            return modelAndView;
        }
        if (userExist.getStatus() == UserStatus.INACTIVE) {
            modelAndView.addObject("error", "Account is inactive");
            return modelAndView;
        }
        session.setAttribute("email", email);
        session.setAttribute(email, RequestType.FORGOT);
        Optional<Request> request = requestService.findByEmailRequest(email);
        request = request.isPresent()
                ? request
                : Optional.of(new Request(email, RequestType.FORGOT));
        Optional<Request> savedRequest = requestService.save(request.get());
        if (savedRequest.isEmpty()) {
            return new ModelAndView("errors/server-error", HttpStatus.NOT_FOUND);
        }
        boolean sendCodeSuccess = requestService.sendCodeToEmail(email,
                "CONFIRM FORGOT PASSWORD", savedRequest.get().getRequestCode());
        if (!sendCodeSuccess) {
            return new ModelAndView("errors/server-error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        modelAndView.setViewName("accounts/activate");
        modelAndView.addObject("email", email);
        modelAndView.addObject("urlTarget", "/forgot-password/confirm");
        return modelAndView;
    }

    @PostMapping("/forgot-password/confirm")
    public ModelAndView confirmForgot(@RequestParam("code") String code, @RequestParam("email") String email) {
        ModelAndView modelAndView = new ModelAndView("accounts/activate");
        User existingUser = userService.findByEmail(email);
        if (existingUser == null) {
            return new ModelAndView("errors/server-error", HttpStatus.NOT_FOUND);
        }
        Optional<Request> request = requestService.findByEmailRequest(email);
        if (request.isEmpty()) {
            modelAndView.addObject("error", "Request don't exist");
            return modelAndView;
        } else {
            if (request.get().getRequestType() != RequestType.FORGOT) {
                modelAndView.addObject("error", "Request don't exist");
                modelAndView.setViewName("accounts/forgot-password");
                return modelAndView;
            }
            long timeOut = System.currentTimeMillis() - request.get().getRequestAt().getTime();
            if (timeOut > 60000) {
                modelAndView.addObject("error", "Request is expired");
                modelAndView.setViewName("accounts/forgot-password");
                return modelAndView;
            }
            if (!request.get().getRequestCode().equals(code)) {
                modelAndView.addObject("error", "Code is incorrect");
                return modelAndView;
            }
        }
        modelAndView.addObject("email", email);
        session.setAttribute(email, "UPDATE_PASSWORD");
        modelAndView.setViewName("accounts/update-password");
        return modelAndView;
    }

    @PostMapping("/forgot-password/update-password")
    public ModelAndView updatePassword(@ModelAttribute UserPasswordUpdateDTO userPasswordUpdateDTO) {
        ModelAndView modelAndView = new ModelAndView("accounts/update-password");
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return new ModelAndView("errors/server-error", HttpStatus.NOT_FOUND);
        }
        if (!session.getAttribute(email).equals("UPDATE_PASSWORD")) {
            return new ModelAndView("errors/server-error", HttpStatus.NOT_FOUND);
        }
        User user = userService.findByEmail(email);
        if (user == null) {
            return new ModelAndView("errors/server-error", HttpStatus.NOT_FOUND);
        }
        if (!userPasswordUpdateDTO.getNewPassword().equals(userPasswordUpdateDTO.getConfirmPassword())) {
            modelAndView.addObject("error", "Password and confirm password are not the same");
            return modelAndView;
        }
        user.setPassword(userPasswordUpdateDTO.getNewPassword());
        Optional<User> userUpdate = userService.save(user);
        if (userUpdate.isEmpty()) {
            return new ModelAndView("errors/server-error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        session.removeAttribute(email);
        session.removeAttribute("email");
        modelAndView.addObject("error", "Change password successfully, please login again");
        return modelAndView;
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
            return new ModelAndView("errors/server-error", HttpStatus.NOT_FOUND);
        }

        boolean sendCodeSuccess = requestService.sendCodeToEmail(userRegisterDTO.getEmail(),
                "CONFIRM REGISTER", savedRequest.get().getRequestCode());
        if (!sendCodeSuccess) {
            return new ModelAndView("errors/server-error", HttpStatus.INTERNAL_SERVER_ERROR);
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
            return new ModelAndView("errors/server-error", HttpStatus.NOT_FOUND);
        }

        if (!isValidActivationRequest(request.get(), code)) {
            modelAndView.addObject("email", email);
            modelAndView.addObject("message", getErrorMessage(request.get(), code));
            return modelAndView;
        }

        user.setStatus(UserStatus.ACTIVE);
        Optional<User> userUpdate = userService.save(user);
        if (userUpdate.isEmpty()) {
            return new ModelAndView("errors/server-error", HttpStatus.INTERNAL_SERVER_ERROR);
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
