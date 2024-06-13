package com.socialnetwork.socialnetworkjavaspring.controllers.apis;

import com.socialnetwork.socialnetworkjavaspring.DTOs.users.UserChangeEmailDTO;
import com.socialnetwork.socialnetworkjavaspring.controllers.ApplicationController;
import com.socialnetwork.socialnetworkjavaspring.models.Request;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.models.enums.RequestType;
import com.socialnetwork.socialnetworkjavaspring.services.requests.IRequestService;
import com.socialnetwork.socialnetworkjavaspring.services.users.IUserService;
import com.socialnetwork.socialnetworkjavaspring.utils.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
@RequestMapping("/api/settings")
public class ApisSettingController extends ApplicationController {

    @Autowired
    private IRequestService requestService;

    @Autowired
    private IUserService userService;

    @Autowired
    private HttpSession session;

    @PostMapping("/change-email/get-code")
    public ResponseEntity<ApiResponse> getCode(@ModelAttribute UserChangeEmailDTO userChangeEmailDTO) {
        User user = userService.findByEmail(userChangeEmailDTO.getOldEmail());
        if (user == null) {
            return responseApi(HttpStatus.BAD_REQUEST, "Old email is not exist!");
        }
        if (!userChangeEmailDTO.getOldEmail().equals(currentUser.getEmail())) {
            return responseApi(HttpStatus.BAD_REQUEST, "Old email and new email are not matched!");
        }
        if (userChangeEmailDTO.getOldEmail().equals(userChangeEmailDTO.getNewEmail())) {
            return responseApi(HttpStatus.BAD_REQUEST, "Old email and new email are the same!");
        }
        User userEmailExits = userService.findByEmail(userChangeEmailDTO.getNewEmail());
        if (userEmailExits != null) {
            return responseApi(HttpStatus.BAD_REQUEST, "New email is already exist!");
        }
        session.setAttribute("email", userChangeEmailDTO.getOldEmail());
        session.setAttribute(userChangeEmailDTO.getOldEmail(), RequestType.CHANGE_EMAIL);
        Optional<Request> request = requestService.findByEmailRequest(userChangeEmailDTO.getNewEmail());
        request = request.isPresent()
                ? request
                : Optional.of(new Request(userChangeEmailDTO.getNewEmail(), RequestType.CHANGE_EMAIL));
        Optional<Request> savedRequest = requestService.save(request.get());
        boolean sendCodeSuccess = requestService.sendCodeToEmail(userChangeEmailDTO.getNewEmail(),
                "CONFIRM CHANGE EMAIL", savedRequest.get().getRequestCode());
        if (!sendCodeSuccess) {
            return responseApi(HttpStatus.BAD_REQUEST, "Send code failed!");
        }
        return responseApi(HttpStatus.OK,
                String.format("We have sent the verification code to your email %s. Please check and enter in the box below to confirm!",
                        userChangeEmailDTO.getNewEmail()));
    }

    @PostMapping("/change-email/confirm-code")
    public ResponseEntity<ApiResponse> verifyCode(@RequestParam String code, @ModelAttribute UserChangeEmailDTO userChangeEmailDTO) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return responseApi(HttpStatus.BAD_REQUEST, "Email is not exist!");
        }
        Optional<Request> request = requestService.findByEmailRequest(userChangeEmailDTO.getNewEmail());
        if (request.isEmpty() || request.get().getRequestType() != RequestType.CHANGE_EMAIL) {
            return responseApi(HttpStatus.BAD_REQUEST, "Request is not exist!");
        }
        if (!request.get().getRequestCode().equals(code)) {
            return responseApi(HttpStatus.BAD_REQUEST, "Code is not correct!");
        }
        currentUser.setEmail(userChangeEmailDTO.getNewEmail());
        userService.save(currentUser);
        session.removeAttribute(email);
        session.removeAttribute("email");
        return responseApi(HttpStatus.OK, "Change email successfully!");
    }
}
