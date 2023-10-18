package com.korit.board.security;

import com.korit.board.service.PrincipalUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrincipalProvider implements AuthenticationProvider {

    private final PrincipalUserDetailsService principalUserDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override // authenticate를 직접 구현 및 인증
    public Authentication authenticate(Authentication authentication) throws AuthenticationException { // AuthenticationException 얘한테 예외미룸
        String email = authentication.getName();
        String password = (String) authentication.getCredentials(); // Object 로 반환되니까 String으로 다운캐스팅 해줘야함

        System.out.println("email: " + email);  // email 확인 getName
        System.out.println("password: " + password);  // password 확인 getCredentials

        UserDetails principalUser = principalUserDetailsService.loadUserByUsername(email);

        // matches : 복호화는 안되지만 , 암호화안된거, 암호화된거를 비교해서 일치한지 비교해줌 순서 (암호화안된비밀번호, 암호화된비밀번호)
        if(!passwordEncoder.matches(password, principalUser.getPassword())) {
            throw new BadCredentialsException("BadCredentials: password 불일치"); // 비밀번호 틀렸다는 예외
        }

        // 이메일, 암호화되지않은 비밀번호, 권한을 이용하여 토큰을 만들어줌
        return new UsernamePasswordAuthenticationToken(principalUser, password, principalUser.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }

}