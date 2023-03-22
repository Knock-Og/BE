package com.project.comgle.exception;

import com.project.comgle.dto.common.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.net.UnknownHostException;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> customException(CustomException e){
        return ResponseEntity.status(e.getExceptionEnum().getCode())
                .body(ErrorResponse.of(e.getExceptionEnum().getCode(),e.getExceptionEnum().getMsg())
                );
    }

    @ExceptionHandler(UnknownHostException.class)
    protected ResponseEntity<ErrorResponse> customException(UnknownHostException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage())
                );
    }

    @ExceptionHandler(HttpClientErrorException.class)
    protected ResponseEntity<ErrorResponse> customException(HttpClientErrorException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value())
                .body(ErrorResponse.of(HttpStatus.NOT_FOUND.value(),e.getMessage())
                );
    }
}
