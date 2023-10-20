package com.korit.board.service;

import com.korit.board.dto.UpdatePasswordReqDto;
import com.korit.board.dto.UpdateProfileImgReqDto;
import com.korit.board.entity.User;
import com.korit.board.exception.AuthMailException;
import com.korit.board.exception.MismatchedPasswordException;
import com.korit.board.jwt.JwtProvider;
import com.korit.board.repository.UserMapper;
import com.korit.board.security.PrincipalUser;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class AccountService {

    private final JwtProvider jwtProvider;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

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

    @Transactional(rollbackFor = Exception.class)
    public boolean updateProfileImg(UpdateProfileImgReqDto updateProfileImgReqDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userMapper.updateProfileImg(User.builder()
                        .email(email)
                        .profileUrl(updateProfileImgReqDto.getProfileUrl())
                        .build()) > 0;
    }

    public boolean updatePassword(UpdatePasswordReqDto updatePasswordReqDto) {
        PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = principalUser.getUser();

        // 기존비밀번호와 변경할 비밀번호가 일치한지 확인
        if(!passwordEncoder.matches(updatePasswordReqDto.getOldPassword(), user.getPassword())) {
            throw new BadCredentialsException("BadCredentials");
        }

        // 새로운 비밀번호와 재입력받은 비밀번호를 확인
        if(Objects.equals(updatePasswordReqDto.getOldPassword(), updatePasswordReqDto.getCheckNewPassword())) {
            throw new MismatchedPasswordException();
        }

        user.setPassword(passwordEncoder.encode(updatePasswordReqDto.getNewPassword()));

        return userMapper.updatePassword(user) > 0;
    }

}
