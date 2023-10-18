package com.korit.board.service;

import com.korit.board.dto.SigninReqDto;
import com.korit.board.dto.SignupReqDto;
import com.korit.board.entity.User;
import com.korit.board.exception.DuplicateException;
import com.korit.board.jwt.JwtProvider;
import com.korit.board.repository.UserMapper;
import com.korit.board.security.PrincipalProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor // 생성자
public class AuthService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PrincipalProvider principalProvider;
    private final JwtProvider jwtProvider;

    public boolean signup(@RequestBody SignupReqDto signupReqDto) {
        User user = signupReqDto.toUserEntity(passwordEncoder);

        int errorCode = userMapper.checkDuplicate(user);
        if(errorCode > 0) {
            responseDuplicateError(errorCode);  // 중복된 값이 있을때 오류코드를 전달함
        }

        return userMapper.saveUser(user) > 0;
    }

    private void responseDuplicateError(int errorCode) {
        Map<String, String> errorMap = new HashMap<>();

        switch (errorCode) {
            case 1: // 이메일 중복
                errorMap.put("email", "이미 사용중인 이메일 입니다.");
                break;
            case 2: // 닉네임 중복
                errorMap.put("nickname", "이미 사용중인 닉네임 입니다.");
                break;
            case 3: // 둘다 중복
                errorMap.put("email", "이미 사용중인 이메일 입니다.");
                errorMap.put("nickname", "이미 사용중인 닉네임 입니다.");
                break;
        }

        throw new DuplicateException(errorMap); // 해당 에외를 던지면 ControllerAdvice가 잡아서 예외처리함
    }

    public String signin(SigninReqDto signinReqDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(signinReqDto.getEmail(), signinReqDto.getPassword()); // 토큰생성

        // 토큰을 갖고 인증 리턴은 authenticate 객체 / authenticate 이 호출되면 loadUserByUsername 호출됨
        // authenticate이 예외를 미루면서 여기서 처리함
        Authentication authentication = principalProvider.authenticate(authenticationToken);

        return jwtProvider.generateToken(authentication);
    }

}
