package com.project.comgle.auth.dto;

import lombok.Getter;

@Getter
public class EmailResponseDto {

    private String email;

    public EmailResponseDto(String email){
        this.email = email;
    }
}


