package com.socialnetwork.socialnetworkjavaspring.controllers.apis;

import com.socialnetwork.socialnetworkjavaspring.DTOs.relations.RelationResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.relations.RelationResponseObjectDTO;
import com.socialnetwork.socialnetworkjavaspring.controllers.ApplicationController;
import com.socialnetwork.socialnetworkjavaspring.models.Relation;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.models.enums.RelationType;
import com.socialnetwork.socialnetworkjavaspring.services.relations.IRelationService;
import com.socialnetwork.socialnetworkjavaspring.services.users.IUserService;
import com.socialnetwork.socialnetworkjavaspring.utils.ConvertUtils;
import com.socialnetwork.socialnetworkjavaspring.utils.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relations")
public class ApiRelationController extends ApplicationController {
    @Autowired
    private IRelationService relationService;

    @Autowired
    private IUserService userService;

    @GetMapping("/request")
    public ResponseEntity<ApiResponse> getRequestFriend() {
        try {
            List<Relation> relations = relationService.findByUserTargetIdAndType(currentUser.getUserId(), RelationType.REQUEST);
            List<RelationResponseObjectDTO> relationResponseDTOs = relationService.findRelationDTOWithMutualFriendCount(currentUser.getUserId(), relations);
            List<RelationResponseDTO> responseDTOs = ConvertUtils.convertList(relationResponseDTOs, RelationResponseDTO.class);
            return responseApi(HttpStatus.OK, "Get request friend successfully", responseDTOs);
        } catch (Exception e) {
            return responseApi(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/request")
    public ResponseEntity<ApiResponse> requestFriend(@RequestParam("id") String userTargetId) {
        try {
            User userTarget = userService.findById(userTargetId);
            List<Relation> relationExits = relationService.findByUserIdAndUserTargetId(currentUser.getUserId(),
                    userTarget.getUserId());
            if (relationExits.size() > 0) {
                relationService.removeAllByUserIdAndUserTargetId(currentUser.getUserId(),
                        userTarget.getUserId());
                return responseApi(HttpStatus.NO_CONTENT, "Delete request friend successfully");
            }
            return handleRequestFriend(userTarget);
        } catch (NullPointerException e) {
            return responseApi(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return responseApi(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/accept")
    public ResponseEntity<ApiResponse> acceptFriend(@RequestParam("user-id") String userRequestId) {
        try {
            User userRequest = checkRequestFriendValid(userRequestId);
            relationService.removeAllByUserIdAndUserTargetId(userRequestId, currentUser.getUserId());
            relationService.acceptFriend(userRequest, currentUser);
            return responseApi(HttpStatus.CREATED, "Accept friend successfully");
        } catch (NullPointerException e) {
            return responseApi(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return responseApi(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/reject")
    public ResponseEntity<ApiResponse> rejectFriend(@RequestParam("user-id") String userRequestId) {
        try {
            checkRequestFriendValid(userRequestId);
            relationService.removeAllByUserIdAndUserTargetId(userRequestId, currentUser.getUserId());
            return responseApi(HttpStatus.NO_CONTENT, "Reject friend successfully");
        } catch (NullPointerException e) {
            return responseApi(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return responseApi(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    private ResponseEntity<ApiResponse> handleRequestFriend(User userTarget) {
        Relation relationRequest = new Relation(RelationType.REQUEST, currentUser, userTarget);
        relationService.save(relationRequest);
        Relation relationFollow = new Relation(RelationType.FOLLOW, currentUser, userTarget);
        relationService.save(relationFollow);
        return responseApi(HttpStatus.CREATED, "Request friend successfully");
    }

    private User checkRequestFriendValid(String userRequestId) throws Exception {
        User userRequest = userService.findById(userRequestId);
        relationService.findByUserIdAndUserTargetIdAndType(userRequestId,
                currentUser.getUserId(), RelationType.REQUEST);
        return userRequest;
    }
}
