package com.learning.gymback.service;

import com.learning.gymback.repository.UserRepository;
import com.learning.gymback.security.entity.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public SecurityUser getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("No user with this username"));
    }

    public SecurityUser getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No user with this id"));
    }

    public List<SecurityUser> getAllUsersByAdmin() {
        return userRepository.findAll();
    }
}
