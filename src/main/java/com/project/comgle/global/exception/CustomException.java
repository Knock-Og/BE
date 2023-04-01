package com.project.comgle.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{
    private final ExceptionEnum exceptionEnum;

}
