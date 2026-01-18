package com.hitachi.drone.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ApiResponse {
    private String message;
    private HttpStatus httpStatus;
}
