package com.korit.board.service;

import com.korit.board.dto.SignupReqDto;
import com.korit.board.entity.User;
import com.korit.board.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor // 생성자
public class AuthService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public boolean signup(@RequestBody SignupReqDto signupReqDto) {
        User user = userMapper.saveUser(signupReqDto.toUserEntity(passwordEncoder);

        switch (userMapper.checkDuplicate(user)) {
            case 1: break;
            case 2: break;
            case 3: break;
        }
        return userMapper.saveUser(user) > 0;
    }
}
