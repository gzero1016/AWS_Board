package com.korit.board.security.oauth2;

import com.korit.board.entity.User;
import com.korit.board.jwt.JwtProvider;
import com.korit.board.repository.UserMapper;
import com.korit.board.security.PrincipalUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler  extends SimpleUrlAuthenticationSuccessHandler {

    private final UserMapper userMapper;
    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String oauth2Id = authentication.getName();
        User user = userMapper.findUserByOauth2Id(oauth2Id);

        if(user == null) {
            DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
            String name = defaultOAuth2User.getAttributes().get("name").toString();
            String profileImg = defaultOAuth2User.getAttributes().get("profile_image").toString();
            String provider = defaultOAuth2User.getAttributes().get("provider").toString();

            // sendRedirect: 키,값을 담아 get요청 http://localhost:3000/auth/oauth2/signup <- 여기로이동
            response.sendRedirect("http://localhost:3000/auth/oauth2/signup" +
                    "?oauth2Id=" + oauth2Id +
                    "&name=" + URLEncoder.encode(name, "UTF-8") +
                    "&profileImg=" + profileImg +
                    "&provider=" + provider);
        }

        PrincipalUser principalUser = new PrincipalUser(user);

        // authentication 객체를 만드려면 :
        // Principal객체 , 암호화되지않은 password(넣지않아도됨) , getAuthorities(권한) 3가지가 필요함
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(principalUser, null, principalUser.getAuthorities());

        String accessToken = jwtProvider.generateToken(authenticationToken);

        // http://localhost:3000/auth/oauth2/login 으로 token을 보냄
        response.sendRedirect("http://localhost:3000/auth/oauth2/login" +
                "?token=" + URLEncoder.encode(accessToken, "UTF-8"));
    }
}
