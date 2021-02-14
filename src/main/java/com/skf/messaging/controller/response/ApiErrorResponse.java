package com.skf.messaging.controller.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
public class ApiErrorResponse implements Serializable {

    private static final long serialVersionUID = -8201938846322366437L;

    private final HttpStatus status;
    private final String message;
}
