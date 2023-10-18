package com.korit.board.controller;

import com.korit.board.aop.annotation.ArgsAop;
import com.korit.board.aop.annotation.retrunAop;
import com.korit.board.aop.annotation.TimeAop;
import com.korit.board.aop.annotation.ValidAop;
import com.korit.board.dto.SigninReqDto;
import com.korit.board.dto.SignupReqDto;
import com.korit.board.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @retrunAop
    @ArgsAop
    @TimeAop
    @ValidAop // 타겟지정
    @PostMapping("/auth/signup")
    public ResponseEntity<?> signup(
            @Valid  @RequestBody SignupReqDto signupReqDto,
            BindingResult bindingResult) {

        return ResponseEntity.ok(authService.signup(signupReqDto));
    }

    @ArgsAop // 클라이언트에게 받아온 데이터를 print 찍어줌
    @PostMapping("/auth/signin")
    public ResponseEntity<?> signin(@RequestBody SigninReqDto signinReqDto) {
        return ResponseEntity.ok(authService.signin(signinReqDto));
    }
}