package com.socialnetwork.socialnetworkjavaspring.DTOs.posts;

import com.socialnetwork.socialnetworkjavaspring.DTOs.common.SearchRequestDTO;
import com.socialnetwork.socialnetworkjavaspring.utils.Constants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SearchPostRequestDTO extends SearchRequestDTO {
    private String content;
    private List<String> hashtags;

    public void validateInput(){
        super.validateInput();
        content = content == null ? Constants.EMPTY_STRING : content.trim();
        hashtags = hashtags == null ? new ArrayList<>() : hashtags;
    }
}
