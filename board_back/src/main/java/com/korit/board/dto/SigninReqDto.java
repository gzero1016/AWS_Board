package com.korit.board.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SigninReqDto {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
