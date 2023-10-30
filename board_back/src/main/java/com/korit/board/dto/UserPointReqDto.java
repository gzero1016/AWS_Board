package com.korit.board.dto;

import com.korit.board.entity.User;
import lombok.Data;

@Data
public class UserPointReqDto {
    private String email;
    private int point;

    public User toUserPoint() {
        return User.builder()
                .email(email)
                .userPoint(point)
                .build();
    }
}
