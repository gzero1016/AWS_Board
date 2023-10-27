package com.korit.board.repository;

import com.korit.board.dto.UpdateProfileImgReqDto;
import com.korit.board.entity.User;
import com.korit.board.entity.UserPoint;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    public Integer saveUser(User user);
    public Integer checkDuplicate(User user);
    public User findUserByEmail(String email);
    public Integer updateEnabledToEmail(String email);
    public Integer updateProfileImg(User user);
    public Integer updatePassword(User user);
    public User findUserByOauth2Id(String oauth2Id);
    public Integer updateOauth2IdAndProvider(User user);
}
