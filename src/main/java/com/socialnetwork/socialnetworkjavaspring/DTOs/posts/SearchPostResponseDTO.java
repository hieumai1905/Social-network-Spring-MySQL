package com.socialnetwork.socialnetworkjavaspring.DTOs.posts;

import com.socialnetwork.socialnetworkjavaspring.DTOs.common.SearchRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SearchPostResponseDTO extends SearchRequestDTO {
    private List<PostResponseDTO> postResponseDTOS;
    private Long totalElements;
}
