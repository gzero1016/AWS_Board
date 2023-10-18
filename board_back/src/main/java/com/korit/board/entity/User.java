package com.korit.board.entity;

import com.korit.board.dto.PrincipalReqDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer userId;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private int enabled;

    public PrincipalReqDto toPrincipalDto() {
        return PrincipalReqDto.builder()
                .userId(userId)
                .email(email)
                .name(name)
                .nickname(nickname)
                .enabled(enabled > 0)
                .build();
    }
}
