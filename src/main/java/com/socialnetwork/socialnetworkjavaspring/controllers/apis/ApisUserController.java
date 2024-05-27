package com.socialnetwork.socialnetworkjavaspring.controllers.apis;

import com.socialnetwork.socialnetworkjavaspring.DTOs.users.ChangeStatusOrRoleUserRequestDTO;
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
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/change-status-user")
    public ResponseEntity<ApiResponse> changeStatusUser(@RequestBody ChangeStatusOrRoleUserRequestDTO request){
        try{
            userService.changeStatusUser(request);
            return responseApi(HttpStatus.OK, "Change status user successfully!");
        }catch (Exception e){
            return responseApi(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/change-role-user")
    public ResponseEntity<ApiResponse> changeRoleUser(@RequestBody ChangeStatusOrRoleUserRequestDTO request){
        try{
            userService.changeRoleUser(request);
            return responseApi(HttpStatus.OK, "Change role user successfully!");
        }catch (Exception e){
            return responseApi(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
