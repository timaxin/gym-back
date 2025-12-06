package com.learning.gymback.repository;

import com.learning.gymback.security.entity.SecurityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface UserRepository extends JpaRepository<SecurityUser, Long> {

    Optional<SecurityUser> findById(Long id);
    Optional<SecurityUser> findByUsername(String username);

}
