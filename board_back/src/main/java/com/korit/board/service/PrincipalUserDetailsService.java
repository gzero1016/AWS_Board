package com.korit.board.service;

import com.korit.board.entity.User;
import com.korit.board.repository.UserMapper;
import com.korit.board.security.PrincipalUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
@RequiredArgsConstructor // IoC 등록하면 무조건 필요하고 final이랑 짝꿍임
public class PrincipalUserDetailsService implements UserDetailsService, OAuth2UserService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userMapper.findUserByEmail(email);

        if (user == null) { // user 값이 null일 경우 예외 터트림
            throw new UsernameNotFoundException("UsernameNotFound: email 불일치");
        }
        return new PrincipalUser(user); // user에 값이 있을경우 PrincipalUser로 리턴해줌
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        String provider = userRequest.getClientRegistration().getClientName();
        response.put("provider", provider);

        System.out.println("attributes: " + attributes);
        System.out.println("response: " + response);

                                    // 순서 : 권한, 정보, 키값
        return new DefaultOAuth2User(new ArrayList<>(), response, "id");
    }

}
