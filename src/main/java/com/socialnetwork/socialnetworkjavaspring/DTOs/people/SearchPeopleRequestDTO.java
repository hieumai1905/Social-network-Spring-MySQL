package com.socialnetwork.socialnetworkjavaspring.DTOs.people;

import com.socialnetwork.socialnetworkjavaspring.utils.Constants;
import com.socialnetwork.socialnetworkjavaspring.DTOs.common.SearchRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SearchPeopleRequestDTO extends SearchRequest {
    private String fullName;
    private String type;

    public SearchPeopleRequestDTO(String fullName) {
        super();
        this.fullName = fullName;
    }

    public void validateInput(){
        super.validateInput();

        fullName = fullName == null ? Constants.EMPTY_STRING : fullName;
    }
}
