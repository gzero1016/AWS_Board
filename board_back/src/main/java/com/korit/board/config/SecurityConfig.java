package com.korit.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity  // 기존 Security 사용하지않고 아래 정의하는 Security를 사용할것이다.
@Configuration  // config 등록
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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
                .permitAll(); //  클라이언트에게 받은 요청을 필터를 거치지않고 컨트롤러로 넘겨주겠다.
    }
}
