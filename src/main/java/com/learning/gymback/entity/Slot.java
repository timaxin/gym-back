package com.learning.gymback.entity;

import com.learning.gymback.security.entity.SecurityUser;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "slots")
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trainer_id", foreignKey = @ForeignKey(name = "fk_slots_trainer"))
    private SecurityUser trainer;

    @Column(name = "start_time")
    private long startTime;
    private long duration;

    @Column(name = "end_time")
    private long endTime;
    private int capacity;
    private String type;
    private boolean canceled;
    private String reason;
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_slots_created_by"))
    private SecurityUser createdBy;

}
