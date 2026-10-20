package com.DingDong.Ding.Dong.Tracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateWorkoutPlanRequest(
        @NotBlank(message = "Plan name is required")
        @Size(max = 150, message = "Plan name must be at most 150 characters")
        String name,

        String description,
        String goal,
        String difficultyLevel,
        String programType,
        Integer estimatedDurationMinutes,
        Integer targetDaysPerWeek,
        List<String> equipmentNeeded,
        String location,
        Boolean isPublic,
        Boolean isTemplate
) {}
