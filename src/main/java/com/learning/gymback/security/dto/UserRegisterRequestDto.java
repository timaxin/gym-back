package com.learning.gymback.security.dto;

import lombok.Data;


@Data
public class UserRegisterRequestDto {

    private String lastName;
    private String firstName;
    private String email;
    private String password;
}
