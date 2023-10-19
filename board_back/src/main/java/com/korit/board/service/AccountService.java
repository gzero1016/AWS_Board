package com.korit.board.service;

import com.korit.board.entity.User;
import com.korit.board.exception.AuthMailException;
import com.korit.board.jwt.JwtProvider;
import com.korit.board.repository.UserMapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AccountService {
    private final JwtProvider jwtProvider;
    private final UserMapper userMapper;

    // 토큰 분해 해서 유효성 검사
    @Transactional(rollbackFor = Exception.class) // 실패시 리셋
    public boolean authenticateMail(String token) {
        Claims claims = jwtProvider.getClaims(token);

        if(claims == null) {
            throw new AuthMailException("만료된 인증 요청입니다.");
        }

        String email = claims.get("email").toString();
        System.out.println(email);
        User user = userMapper.findUserByEmail(email);

        if(user.getEnabled() > 0) {
            throw new AuthMailException("이미 인증이 완료된 요청입니다.");
        }

        return userMapper.updateEnabledToEmail(email) > 0;

    }
}
