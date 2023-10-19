package com.korit.board.config;

import com.korit.board.filter.JwtAuthenticationFilter;
import com.korit.board.security.PrincipalEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity  // 기존 Security 사용하지않고 아래 정의하는 Security를 사용할것이다.
@Configuration  // config 등록
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalEntryPoint principalEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean  // IoC 등록
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors(); // WebMvcConfig의 Cors 설정을 적용
        http.csrf().disable(); // <- 서버사이드 랜더링에서만 사용 (응답하는 데이터에 무조건 토큰이있어야함) 우리는 지금 로컬스토리지에 토큰있음
        http.authorizeRequests()
                .antMatchers("/auth/**")  // 특정 url 지정
                .permitAll() //  클라이언트에게 받은 요청을 필터를 거치지않고 컨트롤러로 넘겨주겠다.
                .anyRequest() // 나머지요청들은
                .authenticated() // jwtAuthenticationFilter 에서 인증을 거쳐야한다. (Security안에 authentication의 객체가 있냐 없냐를 검사)
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()  // 인증과정에서 오류가나면 잡아서
                .authenticationEntryPoint(principalEntryPoint);  // Entry 포인트로 던짐
    }
}
