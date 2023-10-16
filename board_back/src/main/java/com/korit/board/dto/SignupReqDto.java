package com.korit.board.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class SignupReqDto {
    @Email
    @NotBlank
    private String email;
    @NotBlank //빈값인지만 확인
    private String password;
    @NotBlank
    private String name;
    @NotBlank
    private String nickname;
}