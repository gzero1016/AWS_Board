package com.korit.board.service;

import com.korit.board.entity.User;
import com.korit.board.repository.UserMapper;
import com.korit.board.security.PrincipalUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // IoC 등록하면 무조건 필요하고 final이랑 짝꿍임
public class PrincipalUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userMapper.findUserByEmail(email);

        if(user == null) { // user 값이 null일 경우 예외 터트림
            throw new UsernameNotFoundException("UsernameNotFound: email 불일치");
        }

        return new PrincipalUser(user); // user에 값이 있을경우 PrincipalUser로 리턴해줌
    }
}
