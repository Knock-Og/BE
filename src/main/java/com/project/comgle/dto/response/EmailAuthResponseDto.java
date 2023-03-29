package com.project.comgle.dto.response;

import lombok.Getter;

@Getter
public class EmailAuthResponseDto {
    private String password;

    public EmailAuthResponseDto(String password) {
        this.password = password;
    }
}
