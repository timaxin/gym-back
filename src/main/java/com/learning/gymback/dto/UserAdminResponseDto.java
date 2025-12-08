package com.learning.gymback.dto;

import com.learning.gymback.security.constants.Role;
import lombok.Builder;

import java.util.Set;

@Builder
public record UserAdminResponseDto(Long id,
                                   String firstName,
                                   String lastName,
                                   Set<Role> roles,
                                   String email,
                                   String bio,
                                   String phone) {
}
