package com.DingDong.Ding.Dong.Tracker.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddSessionExerciseRequest(
        @NotNull(message = "Exercise ID is required")
        Long exerciseId,

        @NotNull(message = "Order index is required")
        @Min(value = 1, message = "Order index must be at least 1")
        Integer orderIndex,

        Integer supersetGroup,
        Boolean wasPlanned,
        String notes
) {}
