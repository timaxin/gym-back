package com.learning.gymback.service;

import com.learning.gymback.dto.UserAdminResponseDto;
import com.learning.gymback.dto.UserChangeByAdminReqDto;
import com.learning.gymback.entity.UserProfile;
import com.learning.gymback.security.constants.Role;
import com.learning.gymback.security.entity.SecurityUser;
import com.learning.gymback.security.repository.SecurityUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final SecurityUserRepository securityUserRepository;
    private final UserDetailsService userDetailsService;

    public SecurityUser getUserByUsername(String email) {
        Optional<SecurityUser> user = securityUserRepository.findByEmail(email);

        return user.orElseThrow(() -> new IllegalArgumentException("There is no user with this email"));
    }

    public SecurityUser getUserById(long id) {
        return securityUserRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No user with this id"));
    }

    public UserAdminResponseDto changeUserByAdmin(UserChangeByAdminReqDto dto) {
        SecurityUser securityUser = (SecurityUser) userDetailsService.loadUserByUsername(dto.email());
        Set<Role> existingRoles = securityUser.getRoles();

        UserProfile userProfile = securityUser.getProfile();
        userProfile.setFirstName(dto.firstName());
        userProfile.setLastName(dto.lastName());

        if (dto.roles().contains(Role.TRAINER) && !existingRoles.contains(Role.TRAINER)) {
            if (dto.bio() == null || dto.phone() == null) {
                throw new IllegalArgumentException("No bio or phone provided when trying to grant TRAINER role to user {} " + dto.email());
            }

            userProfile.setBio(dto.bio());
            userProfile.setPhone(dto.phone());
        }

        existingRoles.addAll(dto.roles());
        securityUser.setEmail(dto.email());

        securityUserRepository.save(securityUser);

        return UserAdminResponseDto.builder()
                .email(securityUser.getEmail())
                .firstName(userProfile.getFirstName())
                .lastName(userProfile.getLastName())
                .roles(securityUser.getRoles())
                .bio(userProfile.getBio())
                .phone(userProfile.getPhone())
                .build();

    }

    public List<SecurityUser> getAllUsersByAdmin() {
        return securityUserRepository.findAll();
    }
}
