package com.korit.board.controller;

import com.korit.board.aop.annotation.ArgsAop;
import com.korit.board.aop.annotation.retrunAop;
import com.korit.board.aop.annotation.TimeAop;
import com.korit.board.aop.annotation.ValidAop;
import com.korit.board.dto.SignupReqDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthController {

    @retrunAop
    @ArgsAop
    @TimeAop
    @ValidAop // 타겟지정
    @CrossOrigin // 시큐리티 설정을 안해서 crossorigin을 안걸어주면 데이터를 못들고 들어옴
    @PostMapping("/auth/signup")
    public ResponseEntity<?> signup(
            @Valid  @RequestBody SignupReqDto signupReqDto,
            BindingResult bindingResult) {

        System.out.println("AuthController!!!");

        return ResponseEntity.ok(true);
    }
}