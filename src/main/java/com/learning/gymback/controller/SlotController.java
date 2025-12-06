package com.learning.gymback.controller;

import com.learning.gymback.advice.role_check.RoleCheck;
import com.learning.gymback.entity.Slot;
import com.learning.gymback.mapper.SlotMapper;
import com.learning.gymback.security.constants.Role;
import com.learning.gymback.security.dto.SlotCreateRequestDto;
import com.learning.gymback.service.SlotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SlotController {

//GET /slots — list (query: from, to, trainerId, status, page, limit, search)
//GET /slots/:id — details
//POST /slots — create slot (trainer or admin) { trainerId, start, duration, capacity, type (group/individual), location, reason }
//PUT /slots/:id — update slot
//DELETE /slots/:id — delete/cancel slot — must accept { reason, cancelledBy } and create log

    private final SlotService slotService;
    private final SlotMapper slotMapper;

    @PostMapping("/v1/slots")
    public ResponseEntity<Slot> createSlot(@RequestBody SlotCreateRequestDto dto) {
        log.info("/v1/slots/{}", dto);
        Slot saved = slotService.createSlot(dto);

        return saved != null ? ResponseEntity.ok(saved) : ResponseEntity.noContent().build();
    }

    @DeleteMapping("/v1/slots")
    public ResponseEntity<String> deleteSlot(@RequestParam("id") Long id) {
        log.info("DELETE /api/v1/slots?id={}", id);
        return slotService.deleteSlot(id) ? ResponseEntity.ok("Slot with id: " + id + " deleted.")
                : ResponseEntity.badRequest().body("Slot with id: " + id + " NOT deleted.");
    }

    @GetMapping("/v1/slots")
    public ResponseEntity<List<Slot>> getAllSlots() { //todo little dto guest user on frontend
        log.info("/v1/slots/{}");
        List<Slot> slots = slotService.getAllSlots();

        return slots != null ? ResponseEntity.ok(slots) : ResponseEntity.noContent().build();
    }
}
