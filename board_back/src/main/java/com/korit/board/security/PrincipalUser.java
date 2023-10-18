package com.korit.board.security;

import com.korit.board.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class PrincipalUser implements UserDetails {
    private User user;

    public PrincipalUser(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 블랙리스트
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호 5번 틀렸을 때
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled() > 0;
    }
}
