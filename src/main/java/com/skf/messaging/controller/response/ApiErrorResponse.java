package com.skf.messaging.controller.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiErrorResponse {

    private final HttpStatus status;
    private final String message;
}
