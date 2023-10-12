package com.javajober.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.javajober.core.util.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {

    public ApplicationExceptionHandler() {

    }

    @ExceptionHandler(ApplicationException.class)
    protected ResponseEntity<ApiResponse.InvalidResponse> handleApplicationException (ApplicationException e) {
        log.error(e.getStatus().getMessage() + " : {}", e.getInfo());
        return ApiResponse.invalidResponse(e.getStatus(), e.getMessage());
    }
}