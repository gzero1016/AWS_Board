package com.korit.board.dto;

import com.korit.board.entity.UserPoint;
import lombok.Data;

@Data
public class UserPointReqDto {
    private String email;
    private int point;

    public UserPoint toUserPoint() {
        return UserPoint.builder()
                .email(email)
                .point(point)
                .build();
    }
}
