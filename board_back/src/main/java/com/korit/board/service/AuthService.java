package com.korit.board.service;

import com.korit.board.dto.MergeOauth2ReqDto;
import com.korit.board.dto.SigninReqDto;
import com.korit.board.dto.SignupReqDto;
import com.korit.board.entity.User;
import com.korit.board.exception.DuplicateException;
import com.korit.board.jwt.JwtProvider;
import com.korit.board.repository.UserMapper;
import com.korit.board.security.PrincipalProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotBlank;
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
        User user = signupReqDto.toUserEntity(passwordEncoder); // password 암호화해서 user안에 담음

        int errorCode = userMapper.checkDuplicate(user); // email, nickname 중복확인
        if(errorCode > 0) { // 중복이 없어야 0
            responseDuplicateError(errorCode);  // 중복된 값이 있을때 오류코드를 전달함
        }

        return userMapper.saveUser(user) > 0;   // 중복확인 통과 후 정상 insert가 되면 클라이언트에게 반환해줌
    }

    private void responseDuplicateError(int errorCode) {
        Map<String, String> errorMap = new HashMap<>(); // 에러를 담을 Map을 생성함

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
        // 클라이언트에게 받은 이메일과 비밀번호로 authentication 객체를 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(signinReqDto.getEmail(), signinReqDto.getPassword());

        // 토큰을 갖고 인증 리턴은 authenticate 객체 / authenticate 이 호출되면 loadUserByUsername 호출됨
        // authenticate이 예외를 미루면서 여기서 처리함
        Authentication authentication = principalProvider.authenticate(authenticationToken);

        // 사용자 정보를 기반으로 토큰생성해서 반환해줌
        return jwtProvider.generateToken(authentication);
    }

    public boolean authenticate(String token) {
        Claims claims = jwtProvider.getClaims(token); // token 해석해서 claims에 담음
        if(claims == null) { // token이 없거나 해석할 수 없다면 예외 터트림
            throw new JwtException("인증 토큰 유효성 검사 실패");
        }
        // 클레임 정보에서 enabled 키값을 추출하여 있으면 true 없으면 false로 반환됨
        return Boolean.parseBoolean(claims.get("enabled").toString());
    }

    @Transactional(rollbackFor = Exception.class) // 실패시 원상복구
    public boolean mergeOauth2(MergeOauth2ReqDto mergeOauth2ReqDto) {
        User user = userMapper.findUserByEmail(mergeOauth2ReqDto.getEmail());

        // 입력받은 비밀번호와 저장되어있는 사용자 비밀번호가 일치한지 확인
        if(!passwordEncoder.matches(mergeOauth2ReqDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("BadCredentials");
        }

        return userMapper.updateOauth2IdAndProvider(mergeOauth2ReqDto.toUserEntity()) > 0;
    }
}
