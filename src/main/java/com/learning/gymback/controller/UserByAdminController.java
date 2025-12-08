package com.learning.gymback.controller;

import com.learning.gymback.dto.UserAdminResponseDto;
import com.learning.gymback.dto.UserChangeByAdminReqDto;
import com.learning.gymback.mapper.UserForAdminMapper;
import com.learning.gymback.security.entity.SecurityUser;
import com.learning.gymback.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserByAdminController {

//    GET /users/:id — profile (admin может получить резюме, но не личную инфо у других)
// todo   PUT /users/:id — update profile (user or admin)
    private final UserService userService;
    private final UserForAdminMapper userForAdminMapper;

    @GetMapping("/v1/users")
    public ResponseEntity<UserAdminResponseDto> getUserForAdminByUsername(@RequestParam("email") String email) {
        log.info("/v1/users/{}", email);
        SecurityUser securityUser = userService.getUserByUsername(email);
        UserAdminResponseDto userAdminResponseDto = mapToResponseDto(securityUser);

        return userAdminResponseDto != null ? ResponseEntity.ok(userAdminResponseDto) : ResponseEntity.notFound().build();
    }

    @GetMapping("/v1/users/{id}")
    public ResponseEntity<UserAdminResponseDto> getUserForAdminById(@PathVariable("id") long id) {
        log.info("/v1/users/{}", id);
        SecurityUser securityUser = userService.getUserById(id);
        UserAdminResponseDto userAdminResponseDto = mapToResponseDto(securityUser);

        return userAdminResponseDto != null ? ResponseEntity.ok(userAdminResponseDto) : ResponseEntity.notFound().build();
    }

//    user can be granted any role by admin
    @PutMapping("/v1/users")
    public ResponseEntity<UserAdminResponseDto> changeUserByAdmin(@RequestBody UserChangeByAdminReqDto dto) {
        log.info("PUT /v1/users/{}", dto);
        UserAdminResponseDto responseDto = userService.changeUserByAdmin(dto);

        return responseDto != null ? ResponseEntity.ok(responseDto) : ResponseEntity.notFound().build();
    }

    @GetMapping("/v1/users/all")
    public ResponseEntity<List<SecurityUser>> getUsersForAdmin() {
        log.info("/v1/users/all");
        List<SecurityUser> securityUsers = userService.getAllUsersByAdmin();

        return securityUsers != null ? ResponseEntity.ok(securityUsers) : ResponseEntity.notFound().build();
    }

    private UserAdminResponseDto mapToResponseDto(SecurityUser securityUser) {
        return UserAdminResponseDto.builder()
                .lastName(securityUser.getProfile().getLastName())
                .firstName(securityUser.getProfile().getFirstName())
                .email(securityUser.getEmail())
                .id(securityUser.getId())
                .roles(securityUser.getRoles())
                .bio(securityUser.getProfile().getBio())
                .phone(securityUser.getProfile().getPhone())
                .build();
    }


}
