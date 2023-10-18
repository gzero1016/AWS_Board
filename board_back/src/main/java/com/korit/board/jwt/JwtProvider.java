package com.korit.board.jwt;

import com.korit.board.security.PrincipalUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    private final Key key;

    // yml 에 있는 key를 들고오는 방법
    public JwtProvider(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateToken(Authentication authentication) {
        String email = authentication.getName();
        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();

        Date date = new Date(new Date().getTime() + (1000 * 60 * 60 * 24));

        return Jwts.builder()
                .setSubject("AccessToken") // 이름짓기
                .setExpiration(date) // 토큰 유효시간
                .claim("eamil", email) // 키값을 커스텀으로 넣음
                .claim("isEnabled", principalUser.isEnabled())
                .signWith(key, SignatureAlgorithm.HS256) // 알고리즘설정
                .compact();
    }

}