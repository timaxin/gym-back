package com.learning.gymback.security.service;

import com.learning.gymback.entity.user_profiles.UserProfile;
import com.learning.gymback.repository.UserProfileRepository;
import com.learning.gymback.security.constants.Role;
import com.learning.gymback.security.dto.UserAuthRequestDto;
import com.learning.gymback.security.dto.UserRegisterRequestDto;
import com.learning.gymback.security.entity.SecurityUser;
import com.learning.gymback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final UserProfileRepository profileRepository;

    public SecurityUser register(UserRegisterRequestDto dto) {
        UserProfile profile = mapToProfile(dto);
        SecurityUser securityUser = mapToUser(dto, profile);

        return userRepository.save(securityUser);
    }

    public String auth(UserAuthRequestDto dto) {
        SecurityUser securityUser = (SecurityUser) userDetailsService.loadUserByUsername(dto.getUsername());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        return jwtService.generateJwtToken(dto.getUsername());
    }

    private SecurityUser mapToUser(UserRegisterRequestDto dto, UserProfile profile) {
        return SecurityUser.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .roles(List.of(Role.USER))
                .profile(profile)
                .build();
    }

    private UserProfile mapToProfile(UserRegisterRequestDto dto) {
        return UserProfile.builder()
                .email(dto.getEmail())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .build();
    }

}
