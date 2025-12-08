package com.learning.gymback.security.service;

import com.learning.gymback.entity.user_profiles.UserProfile;
import com.learning.gymback.security.constants.Role;
import com.learning.gymback.security.dto.UserAuthRequestDto;
import com.learning.gymback.security.dto.UserRegisterRequestDto;
import com.learning.gymback.security.dto.UserRegisterResponseDto;
import com.learning.gymback.security.entity.SecurityUser;
import com.learning.gymback.security.repository.SecurityUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final SecurityUserRepository securityUserRepository;

    public UserRegisterResponseDto register(UserRegisterRequestDto dto) {
        //checks
        if (securityUserRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with same email exists");
        }

        UserProfile profile = mapToProfile(dto);
        SecurityUser securityUser = mapToUser(dto, profile);

        SecurityUser user = securityUserRepository.save(securityUser);

        return UserRegisterResponseDto.builder()
                .id(user.getId())
                .roles(user.getRoles())
                .firstName(user.getProfile().getFirstName())
                .lastName(user.getProfile().getLastName())
                .email(user.getEmail())
                .build();
    }

    public String auth(UserAuthRequestDto dto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        return jwtService.generateJwtToken(dto.getEmail());
    }

    private SecurityUser mapToUser(UserRegisterRequestDto dto, UserProfile profile) {
        return SecurityUser.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .roles(Set.of(Role.USER))
                .profile(profile)
                .createdAt(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
                .build();
    }

    private UserProfile mapToProfile(UserRegisterRequestDto dto) {
        return UserProfile.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .build();
    }

}
