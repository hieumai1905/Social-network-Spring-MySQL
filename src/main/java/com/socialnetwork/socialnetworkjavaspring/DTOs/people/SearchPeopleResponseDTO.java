package com.socialnetwork.socialnetworkjavaspring.DTOs.people;

import com.socialnetwork.socialnetworkjavaspring.DTOs.common.SearchRequest;
import com.socialnetwork.socialnetworkjavaspring.DTOs.users.UserResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SearchPeopleResponseDTO extends SearchRequest {
    private List<UserResponseDTO> userResponses;
    private Long totalElements;

    public SearchPeopleResponseDTO(Integer pageIndex, Integer pageSize, long totalElements, List<UserResponseDTO> userResponses) {
        super(pageIndex, pageSize);
        this.userResponses = userResponses;
        this.totalElements = totalElements;
    }
}