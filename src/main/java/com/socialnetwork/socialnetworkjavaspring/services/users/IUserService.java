package com.socialnetwork.socialnetworkjavaspring.services.users;

import com.socialnetwork.socialnetworkjavaspring.DTOs.people.SearchPeopleRequestDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.people.SearchPeopleResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.services.IGeneralService;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends IGeneralService<User, String>, UserDetailsService {
    SearchPeopleResponseDTO findByFullNameLikeIgnoreCaseAndAccents(SearchPeopleRequestDTO request, String userId);
}