package com.korit.board.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class PrincipalEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        System.out.println(authException.getClass()); // <- 이친구 클래스타입 찍어봐야함

        response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 접근이 금지되었다는 응답코드
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("unauthorized", "인증 실패");

        ObjectMapper objectMapper = new ObjectMapper();

        // writeValueAsString : Map을 JSON 으로 바꿔준다.
        response.getWriter().println(objectMapper.writeValueAsString(errorMap));
    }
}
