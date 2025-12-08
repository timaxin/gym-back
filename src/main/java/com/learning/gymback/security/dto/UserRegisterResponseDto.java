package com.learning.gymback.security.dto;

import com.learning.gymback.security.constants.Role;
import lombok.Builder;

import java.util.Set;

@Builder
public record UserRegisterResponseDto(Long id,
                                      String firstName,
                                      String lastName,
                                      String email,
                                      Set<Role> roles) {
}
