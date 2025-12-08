package com.learning.gymback.security.dto;

import lombok.Data;

@Data
public class UserAuthRequestDto {
    private String email;
    private String password;
}
