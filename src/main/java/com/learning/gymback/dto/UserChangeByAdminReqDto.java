package com.learning.gymback.dto;

import com.learning.gymback.security.constants.Role;

import java.util.List;

public record UserChangeByAdminReqDto(Long id,
                                      String firstName,
                                      String lastName,
                                      List<Role> roles,
                                      String email,
                                      String bio,
                                      String phone) {}
