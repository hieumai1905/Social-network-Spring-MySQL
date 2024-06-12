package com.socialnetwork.socialnetworkjavaspring.services.users;

import com.socialnetwork.socialnetworkjavaspring.DTOs.people.SearchPeopleRequestDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.people.SearchPeopleResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.users.ChangeStatusOrRoleUserRequestDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.users.UserRegisterDTO;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.models.enums.RelationType;
import com.socialnetwork.socialnetworkjavaspring.services.IGeneralService;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface IUserService extends IGeneralService<User, String>, UserDetailsService {
    User findByEmail(String email);
    SearchPeopleResponseDTO findByFullNameLikeIgnoreCaseAndAccents(SearchPeopleRequestDTO request, String userId);
    List<User> findUsersByRelationType(String userId, RelationType type);

    User findById(String userId) throws Exception;
    void changeStatusUser(ChangeStatusOrRoleUserRequestDTO request);
    void changeRoleUser(ChangeStatusOrRoleUserRequestDTO request);

    List<User> findAllUserBlocked(String userId);

    User findUserByEmailAndPassword(String email, String password);

    Optional<User> registerUser(UserRegisterDTO userRegisterDTO);
}
