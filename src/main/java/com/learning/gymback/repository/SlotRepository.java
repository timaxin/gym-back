package com.learning.gymback.repository;

import com.learning.gymback.entity.Slot;
import com.learning.gymback.security.entity.SecurityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {

    @Query("select s from Slot s where s.trainer = ?1 and ((s.startTime between ?2 and ?3) or (s.endTime between ?2 and ?3) or (s.startTime <= ?2 and s.endTime >= ?3))")
    List<Slot> findTimeConflicts(SecurityUser trainer, long startTime, long endTime);
}
