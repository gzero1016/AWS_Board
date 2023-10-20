package com.korit.board.controller;

import com.korit.board.exception.AuthMailException;
import com.korit.board.exception.DuplicateException;
import com.korit.board.exception.MismatchedPasswordException;
import com.korit.board.exception.ValidException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(ValidException.class)
    public ResponseEntity<?> validException(ValidException validException) {
        System.out.println("예외처리 !");
        return ResponseEntity.badRequest().body(validException.getErrorMap());
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<?> duplicateException(DuplicateException duplicateException) {
        System.out.println("이메일, 닉네임 중복검사 오류 예외처리");
        return ResponseEntity.badRequest().body(duplicateException.getErrorMap());
    }

    @ExceptionHandler(UsernameNotFoundException.class) // email 불일치
    public ResponseEntity<?> usernameNotFoundException(UsernameNotFoundException usernameNotFoundException) {
        Map<String, String> message = new HashMap<>();
        message.put("authError", "사용자 정보를 확인해주세요.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message); // Status -> UNAUTHORIZED: 401
    }

    @ExceptionHandler(BadCredentialsException.class) // password 불일치
    public ResponseEntity<?> badCredentialsException(BadCredentialsException badCredentialsException) {
        Map<String, String> message = new HashMap<>();
        message.put("authError", "사용자 정보를 확인해주세요.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message); // Status -> UNAUTHORIZED: 401
    }

    @ExceptionHandler(DisabledException.class) // 이메일 인증 하지않았을 경우
    public ResponseEntity<?> disabledException(DisabledException disabledException) {
        Map<String, String> message = new HashMap<>();
        message.put("disabled", "이메일 인증이 필요합니다.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message); // Status -> FORBIDDEN: 403
    }

    @ExceptionHandler(JwtException.class) // token 유효성 검사
    public ResponseEntity<?> jwtException(JwtException jwtException) {
        Map<String, String> message = new HashMap<>();
        message.put("jwt", "인증이 유효하지 않습니다.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message); // Status -> UNAUTHORIZED: 401
    }

    @ExceptionHandler(AuthMailException.class) // token 유효성 검사
    public ResponseEntity<?> mailException(AuthMailException mailException) {
        Map<String, String> message = new HashMap<>();
        message.put("mailException", mailException.getMessage());
        return ResponseEntity.ok().body(message); // ok로 안주면 오류뜸
    }

    @ExceptionHandler(MismatchedPasswordException.class) // token 유효성 검사
    public ResponseEntity<?> mismatchedPasswordException(MismatchedPasswordException mismatchedPasswordException) {
        Map<String, String> message = new HashMap<>();
        message.put("mismatched", mismatchedPasswordException.getMessage());
        return ResponseEntity.badRequest().body(message);
    }

}
