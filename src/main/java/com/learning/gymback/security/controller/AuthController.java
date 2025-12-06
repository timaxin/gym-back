package com.learning.gymback.security.controller;

import com.learning.gymback.security.entity.SecurityUser;
import com.learning.gymback.security.dto.UserAuthRequestDto;
import com.learning.gymback.security.dto.UserRegisterRequestDto;
import com.learning.gymback.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

//    POST /auth/register — { name, email, password } → 201 + { user, token }
//    POST /auth/login — { email, password } → 200 + { user, token }
//    POST /auth/refresh — refresh token

    private final AuthService authService;

    @PostMapping("/v1/auth/register")
    public ResponseEntity<SecurityUser> register(@RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        log.info("/v1/auth/register: {}", userRegisterRequestDto);
        //todo check same username/names/email
        SecurityUser securityUser = authService.register(userRegisterRequestDto);

        return ResponseEntity.ok(securityUser);
    }

    @PostMapping("/v1/auth/login")
    public ResponseEntity<String> auth(@RequestBody UserAuthRequestDto dto) {
        log.info("v1/auth/login: {}", dto);
        return ResponseEntity.ok(authService.auth(dto));
    }



}
