package com.socialnetwork.socialnetworkjavaspring.services.users;

import com.socialnetwork.socialnetworkjavaspring.DTOs.people.SearchPeopleRequestDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.people.SearchPeopleResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.users.UserResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.models.CustomUserDetails;
import com.socialnetwork.socialnetworkjavaspring.models.Relation;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.models.enums.RelationType;
import com.socialnetwork.socialnetworkjavaspring.repositories.IUserRepository;
import com.socialnetwork.socialnetworkjavaspring.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public Optional<User> save(User object) {
        return Optional.of(userRepository.save(object));
    }

    @Override
    public Optional<User> delete(User object) {
        try {
            userRepository.delete(object);
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return new CustomUserDetails(user.get());
        }
        return null;
    }

    @Transactional
    public UserDetails loadUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + userId)
        );

        return new CustomUserDetails(user);
    }

    public SearchPeopleResponseDTO findByFullNameLikeIgnoreCaseAndAccents(SearchPeopleRequestDTO request, String userId){
        request.validateInput();
        Page<User> users = userRepository
                .findByFullNameLikeIgnoreCaseAndAccents(request.getFullName(), userId,
                        PageRequest.of(request.getPageIndex(), request.getPageSize()));
        List<UserResponseDTO> userResponses = new ArrayList<>();
        for (User user : users) {
            userResponses.add(new UserResponseDTO(
                    user.getUserId(),
                    user.getFullName(),
                    user.getAvatar(),
                    isFriendWithCurrentUser(user.getRelations(), userId
                    )
            ));
        }
        return new SearchPeopleResponseDTO(request.getPageIndex(),request.getPageSize(), users.getTotalElements(),
                userResponses);
    }

    private Boolean isFriendWithCurrentUser(List<Relation> relations, String userId) {
        if(relations != Constants.NULL_OBJECT && relations.size() > Constants.NUMBER_ZERO){
            for (Relation relation : relations) {
                if(relation.getUserTarget().getUserId().equals(userId)
                        && relation.getType().equals(RelationType.FRIEND))
                    return true;
            }
        }
        return false;
    }
}
