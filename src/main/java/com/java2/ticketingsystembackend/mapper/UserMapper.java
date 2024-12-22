package com.java2.ticketingsystembackend.mapper;

import com.java2.ticketingsystembackend.dto.UserInfoDTO;
import com.java2.ticketingsystembackend.entity.User;

public class UserMapper {
    public static UserInfoDTO toUserInfoDTO(User user) {
        return new UserInfoDTO(
                user.getUuid(),
                user.getUsername(),
                user.getEmail(),
                user.getFullname(),
                user.getTel(),
                user.getAddress(),
                user.getRole().getName()
        );
    }
}
