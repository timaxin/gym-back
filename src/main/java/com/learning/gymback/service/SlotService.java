package com.learning.gymback.service;

import com.learning.gymback.entity.Slot;
import com.learning.gymback.mapper.SlotMapper;
import com.learning.gymback.repository.SlotRepository;
import com.learning.gymback.security.constants.Role;
import com.learning.gymback.security.repository.SecurityUserRepository;
import com.learning.gymback.dto.SlotCreateRequestDto;
import com.learning.gymback.security.entity.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlotService {

    private final SlotRepository slotRepository;
    private final SecurityUserRepository securityUserRepository;
    private final SlotMapper slotMapper;


    public Slot createSlot(SlotCreateRequestDto dto) {
        SecurityUser trainer = securityUserRepository.findById(dto.trainerId())
                .orElseThrow(() -> new IllegalArgumentException("No trainer with this id found"));

        if (!trainer.getRoles().contains(Role.TRAINER)) {
            throw new IllegalArgumentException("User with this Id doesn`t have role TRAINER");
        }

        List<Slot> conflicts = slotRepository.findTimeConflicts(trainer, dto.startTime(), dto.endTime());
        if (conflicts != null && !conflicts.isEmpty()) {
            throw new IllegalArgumentException("There are already slots in this time period, reschedule");
        }

        Slot slot = slotMapper.toEntity(dto);
        slot.setTrainer(trainer);

        Slot saved = slotRepository.save(slot);

        return saved;
    }

    public List<Slot> getAllSlots() {
        return slotRepository.findAll();
    }

    public boolean deleteSlot(long id) {

        Optional<Slot> toBeDeleted = slotRepository.findById(id);

        if (toBeDeleted.isEmpty()) {
            return false;
        }
        slotRepository.delete(toBeDeleted.get());

        return true;
    }


}
