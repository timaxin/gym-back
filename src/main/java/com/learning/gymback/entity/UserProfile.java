package com.learning.gymback.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "user_profiles")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

//    @OneToOne(mappedBy = "userProfile", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
//    private Trainer trainer;
    private String bio;
    private String phone;

//    public void setTrainer(Trainer trainer) {
//        if (trainer == null) {
//            if (this.trainer != null) {
//                this.trainer.setUserProfile(null);
//            }
//            this.trainer = null;
//        } else {
//            trainer.setUserProfile(this);
//            this.trainer = trainer;
//        }
//    }
}
