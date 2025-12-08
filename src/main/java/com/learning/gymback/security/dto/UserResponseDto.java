package com.learning.gymback.security.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UserResponseDto {

    private Long id;
    private String email;
    private String role;
    private Long createdAt;

}
