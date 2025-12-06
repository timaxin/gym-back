package com.learning.gymback.entity.user_profiles;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "trainers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Trainer extends UserProfile {
    // trainer-specific fields
    private String bio;
    private String phone;
    // other trainer data (certificates, rating, ...)
}
