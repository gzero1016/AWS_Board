package com.korit.board.controller;

import com.korit.board.dto.PrincipalReqDto;
import com.korit.board.entity.User;
import com.korit.board.security.PrincipalUser;
import com.korit.board.service.AccountService;
import com.korit.board.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final MailService mailService;
    private final AccountService accountService;

    @GetMapping("/account/principal")
    public ResponseEntity<?> getPrincipal() {
        PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = principalUser.getUser();
        PrincipalReqDto principalRespDto = user.toPrincipalDto();

        return ResponseEntity.ok(principalRespDto);
    }

    @PostMapping("/account/mail/auth")
    public ResponseEntity<?> sendAuthenticationMail() {
        return ResponseEntity.ok(mailService.sendAuthMail()); // response에 응답을 mailToken을 날려줌
    }

    @GetMapping("/auth/mail")
    public ResponseEntity<?> authenticateMail(String token) {
        return ResponseEntity.ok(accountService.authenticateMail(token) ? "인증이 완료되었습니다." : "인증 실패");
    }
}