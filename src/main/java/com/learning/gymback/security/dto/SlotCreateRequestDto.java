package com.learning.gymback.security.dto;

public record SlotCreateRequestDto(
        long trainerId,
        long startTime,
        long duration,
        long endTime,
        int capacity,
        String type,
        String location,
        String reason) {}