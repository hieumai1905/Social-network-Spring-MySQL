package com.socialnetwork.socialnetworkjavaspring.DTOs.common;

import com.socialnetwork.socialnetworkjavaspring.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequestDTO {
    private Integer pageIndex;
    private Integer pageSize;

    public void validateInput(){
        if(pageIndex == null || pageIndex < Constants.NUMBER_ZERO)
            pageIndex = Constants.DEFAULT_PAGE_INDEX;
        if(pageSize == null || pageSize <=  Constants.NUMBER_ZERO)
            pageSize = Constants.DEFAULT_PAGE_SIZE;
    }
}
