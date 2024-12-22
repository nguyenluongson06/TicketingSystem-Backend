package com.java2.ticketingsystembackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserInfoDTO {
    private String uuid;
    private String username;
    private String email;
    private String fullName;
    private String tel;
    private String address;
    private String role;


    public UserInfoDTO(String uuid, String username, String email, String fullName, String tel, String address, String role) {
        this.uuid = uuid;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.tel = tel;
        this.address = address;
        this.role = role;
    }
}
