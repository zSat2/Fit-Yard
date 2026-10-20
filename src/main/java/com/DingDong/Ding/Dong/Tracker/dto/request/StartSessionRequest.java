package com.DingDong.Ding.Dong.Tracker.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record StartSessionRequest(
        Long workoutPlanId,

        @NotNull(message = "Started at timestamp is required")
        LocalDateTime startedAt,

        String locationType,
        String notes
) {}
