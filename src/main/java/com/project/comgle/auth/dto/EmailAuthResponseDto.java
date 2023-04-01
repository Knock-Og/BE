package com.project.comgle.auth.dto;

import lombok.Getter;

@Getter
public class EmailAuthResponseDto {
    private String password;

    public EmailAuthResponseDto(String password) {
        this.password = password;
    }

}
