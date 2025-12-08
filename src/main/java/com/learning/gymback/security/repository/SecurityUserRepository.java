package com.learning.gymback.security.repository;

import com.learning.gymback.security.constants.Role;
import com.learning.gymback.security.entity.SecurityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RepositoryRestResource(exported = false)
public interface SecurityUserRepository extends JpaRepository<SecurityUser, Long> {

    Optional<SecurityUser> findById(Long id);

    Optional<SecurityUser> findByRolesAndId(Set<Role> roles, long id);

    Optional<SecurityUser> findByEmail(String email);
}
