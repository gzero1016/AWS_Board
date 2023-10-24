package com.korit.board.filter;


import com.korit.board.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter {

    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        // bearer가 붙어있는 Token
        String bearerToken = httpServletRequest.getHeader("Authorization");
        System.out.println("bearerToken: " + bearerToken);

        // bearer를 뺀 오리지널 token
        String token = jwtProvider.getToken(bearerToken);
        System.out.println("token: " + token);

        // principal + 암호화되지않은 password (제외가능함) + Authorities 를 받아 authentication 객체를 생성함
        Authentication authentication = jwtProvider.getAuthentication(token);
        System.out.println("authentication: " + authentication);

        if(authentication != null) {
            // SecurityContextHolder에 Provider에서 해체된 토큰의 정보를 인증해서 넣어줌 정보가있으면 인증완료
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}