package com.korit.board.jwt;

import com.korit.board.entity.User;
import com.korit.board.repository.UserMapper;
import com.korit.board.security.PrincipalUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    private final Key key;
    private final UserMapper userMapper;

    // yml 에 있는 key를 들고오는 방법
    public JwtProvider(@Value("${jwt.secret}") String secret, @Autowired UserMapper userMapper) {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.userMapper = userMapper;
    }

    // Authentication: Spring Security와 같은 보안 프레임워크에서 사용되며,
                    // 현재 사용자의 인증 및 권한 정보를 포함하는 인터페이스
    public String generateToken(Authentication authentication) { // 매개변수로 현재 사용자의 인증 정보를 받음
        String email = authentication.getName(); // 받아온 정보의 getName안에있는 email을 추출함

        // token 유효시간 설정 아래는 24시간
        Date date = new Date(new Date().getTime() + (1000 * 60 * 60 * 24));

        return Jwts.builder() // 토큰 생성
                .setSubject("AccessToken") // 토큰의 서브젝트(Subject) 설정 : 토큰이름
                .setExpiration(date) // 토큰의 유효시간 설정
                .claim("email", email) // 토큰 내에 이메일 정보를 포함
                .signWith(key, SignatureAlgorithm.HS256) // 서명 알고리즘 설정
                .compact(); // 생성된 토큰을 문자열로 클라이언트에게 반환
    } // Jwt에는 최대한 공개되도되는 최소한의 정보로 만들어야함

    public Claims getClaims(String token) { // JWT 해석하는 로직 유효기간이 지났거나 위조가 되었을 경우 예외 터트림
        Claims claims = null; // 초기화

        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(key) // JWT의 서명 검증에 사용될 키 설정
                    .build()
                    .parseClaimsJws(token) // 주어진 JWT 문자열 파싱 및 서명 확인
                    .getBody(); // JWT에서 클레임 정보 추출하여 'claims' 객체에 저장
        }catch(Exception e) {
            System.out.println(e.getClass() + ": " + e.getMessage());
        }
        return claims;
    }

    public String getToken(String bearerToken) {
        if(!StringUtils.hasText(bearerToken)) {
            return null;
        }
        return bearerToken.substring("Bearer ".length());
    }

    // getClaims 에서 해석된 리턴값을 가지고와서 토큰이 비었는지 검사
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        if(claims == null) {
            return null;
        }
        // 데이터베이스에서 email 꺼내옴
        User user = userMapper.findUserByEmail(claims.get("email").toString());
        if(user == null) {
            return null;
        }
        // 꺼내온 email을 principalUser에 user를 집어넣음
        PrincipalUser principalUser = new PrincipalUser(user);

        return new UsernamePasswordAuthenticationToken(principalUser, null, principalUser.getAuthorities());
    }

    public String generateAuthMailToken(String email) {
        Date date = new Date(new Date().getTime() + 1000 * 60 * 5); // 토큰 만료기간 5분

        return Jwts.builder()
                .setSubject("AuthenticationEmailToken") // 토큰이름
                .setExpiration(date) // 만료시간
                .claim("email", email) // 토큰에 포함할 내용
                .signWith(key, SignatureAlgorithm.HS256) // 알고리즘, 키 설정
                .compact(); // 토큰 생성
    }
}