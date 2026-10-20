package com.DingDong.Ding.Dong.Tracker.dto.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateBodyMetricRequest(
        BigDecimal weightKg,
        BigDecimal bodyFatPct,
        BigDecimal heightCm,
        String measurementTime,
        String measurementMethod,
        String notes,

        @NotNull(message = "Recorded at timestamp is required")
        LocalDateTime recordedAt
) {}
