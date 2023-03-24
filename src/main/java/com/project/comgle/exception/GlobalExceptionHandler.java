package com.project.comgle.exception;

import com.project.comgle.dto.common.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.net.UnknownHostException;
import java.util.Objects;

@RestController
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> customException(CustomException e){
        log.error("CustomException error = {}" , e.getMessage());
        return ResponseEntity.status(e.getExceptionEnum().getCode())
                .body(ErrorResponse.of(e.getExceptionEnum().getCode(),e.getExceptionEnum().getMsg())
                );
    }

    @ExceptionHandler(UnknownHostException.class)
    protected ResponseEntity<ErrorResponse> customException(UnknownHostException e){
        log.error("UnknownHostException error = {}" , e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage())
                );
    }

    @ExceptionHandler(HttpClientErrorException.class)
    protected ResponseEntity<ErrorResponse> customException(HttpClientErrorException e){
        log.error("HttpClientErrorException error = {}" , e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value())
                .body(ErrorResponse.of(HttpStatus.NOT_FOUND.value(),e.getMessage())
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validException(MethodArgumentNotValidException e){
        log.error("MethodArgumentNotValidException error msg = {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                .body(ErrorResponse.of(HttpStatus.NOT_FOUND.value(), Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage())
                );
    }
}
