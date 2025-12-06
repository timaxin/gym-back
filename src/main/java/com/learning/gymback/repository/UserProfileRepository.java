package com.learning.gymback.repository;

import com.learning.gymback.entity.user_profiles.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository <UserProfile, Long> {
}
