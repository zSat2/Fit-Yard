package com.DingDong.Ding.Dong.Tracker.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record LogSetRequest(
        @NotNull(message = "Set number is required")
        @Min(value = 1, message = "Set number must be at least 1")
        Integer setNumber,

        BigDecimal weightKg,
        Integer repsCompleted,
        Integer durationSeconds,
        Integer distanceMeters,
        Integer rpe,
        Boolean isCompleted,
        String notes
) {}
