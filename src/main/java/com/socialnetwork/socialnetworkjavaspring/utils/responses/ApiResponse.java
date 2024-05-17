package com.socialnetwork.socialnetworkjavaspring.utils.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private Integer code;
    private String message;
    private Object data;

    public ApiResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
