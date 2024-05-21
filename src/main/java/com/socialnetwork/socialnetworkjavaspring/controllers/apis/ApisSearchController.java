package com.socialnetwork.socialnetworkjavaspring.controllers.apis;

import com.socialnetwork.socialnetworkjavaspring.DTOs.people.SearchPeopleRequestDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.people.SearchPeopleResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.controllers.ApplicationController;
import com.socialnetwork.socialnetworkjavaspring.services.users.IUserService;
import com.socialnetwork.socialnetworkjavaspring.utils.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
public class ApisSearchController extends ApplicationController {

    @Autowired
    private IUserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse> findByFullName(@ModelAttribute SearchPeopleRequestDTO request){
        SearchPeopleResponseDTO response = userService.findByFullNameLikeIgnoreCaseAndAccents(request, currentUser.getUserId());
        return responseApi(HttpStatus.OK, "Search people successfully!", response);
    }
}
