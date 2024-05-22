package com.socialnetwork.socialnetworkjavaspring.controllers.apis;

import com.socialnetwork.socialnetworkjavaspring.DTOs.users.UserResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.controllers.ApplicationController;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.models.enums.RelationType;
import com.socialnetwork.socialnetworkjavaspring.services.users.IUserService;
import com.socialnetwork.socialnetworkjavaspring.utils.ConvertUtils;
import com.socialnetwork.socialnetworkjavaspring.utils.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class ApisUserController extends ApplicationController {

    @Autowired
    private IUserService userService;

    @GetMapping("/friends")
    public ResponseEntity<ApiResponse> findUsersFriend(){
        List<User> usersFriend = userService.findUsersByRelationType(currentUser.getUserId(), RelationType.FRIEND);
        List<UserResponseDTO> userResponseDTOs = ConvertUtils.convertList(usersFriend, UserResponseDTO.class);
        return responseApi(HttpStatus.OK, "Search people successfully!", userResponseDTOs);
    }
}
