package com.java2.ticketingsystembackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignupRequestDTO {
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(min = 6, max = 50)
    private String password;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String fullname;

    @NotBlank
    private String tel;

    @NotBlank
    private String address;
}

