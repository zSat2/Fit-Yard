package com.DingDong.Ding.Dong.Tracker.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record AddPlanExerciseRequest(
        @NotNull(message = "Exercise ID is required")
        Long exerciseId,

        @NotNull(message = "Order index is required")
        @Min(value = 1, message = "Order index must be at least 1")
        Integer orderIndex,

        Integer supersetGroup,
        Integer targetSets,
        Integer targetRepsMin,
        Integer targetRepsMax,
        BigDecimal targetWeightKg,
        Integer restSeconds,
        Integer restSecondsMax,
        Integer targetDurationSeconds,
        Integer targetDistanceMeters,
        Integer targetCalories,
        Integer targetRpe,
        String tempo,
        String notes,
        List<String> formCues,
        Boolean isOptional,
        Boolean isWarmup,
        Boolean isCooldown
) {}
