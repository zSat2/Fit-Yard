package com.DingDong.Ding.Dong.Tracker.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateExerciseRequest(
        @NotBlank(message = "Exercise name is required")
        String name,

        String description,

        @NotBlank(message = "Category is required")
        String category,

        @NotBlank(message = "Primary muscle group is required")
        String primaryMuscleGroup,

        List<String> secondaryMuscles,
        String movementPattern,
        String equipment,
        Boolean isBodyweight,
        Boolean isUnilateral,
        Boolean isCompound,
        String difficultyLevel,
        Boolean requiresSpotter,
        String defaultTrackingMode,
        Boolean supportsWeight,
        Boolean supportsReps,
        Boolean supportsDuration,
        Boolean supportsDistance,
        List<String> injuryRisks,
        List<String> coachingCues,
        List<String> commonMistakes,
        String setupInstructions,
        List<String> executionSteps,
        String videoUrl,
        String thumbnailUrl
) {}
