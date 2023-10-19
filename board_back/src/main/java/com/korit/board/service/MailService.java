package com.korit.board.service;

import com.korit.board.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final JwtProvider jwtProvider;

    public boolean sendAuthMail() {
        String toEmail = SecurityContextHolder.getContext().getAuthentication().getName(); // 현재 로그인 계정의 이메일 추출
        System.out.println("toEmail: " + toEmail);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8"); // 생성하려면 예외처리 필수
            helper.setSubject("스프링 부트 사용자 인증 메일 테스트"); // Mail 제목
            helper.setFrom("gzero1016@gmail.com"); // 관리자 이메일
            helper.setTo(toEmail); // 전송할 이메일
            String token = jwtProvider.generateAuthMailToken(toEmail); // 토큰생성 후 token변수에 집어넣음

            mimeMessage.setText(
                    "<div>" +
                            "<h1>사용자 인증 메일</h1>" +
                            "<p>사용자 인증을 완료하려면 아래의 버튼을 클릭하세요</p>" +
                            "<a href=\"http://localhost:8080/auth/mail?token=" + token + "\">인증하기</a>" +
                    "</div>", "utf-8", "html"
            );
            javaMailSender.send(mimeMessage); // MailToken 전송
        }catch (Exception e) {
            return false;
        }
        return true;
    }
}