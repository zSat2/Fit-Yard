package com.DingDong.Ding.Dong.Tracker.dto.response;

import com.DingDong.Ding.Dong.Tracker.entity.WorkoutPlanExercise;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PlanExerciseResponse(
        Long id,
        Long workoutPlanId,
        Long exerciseId,
        String exerciseName,
        String exerciseCategory,
        Integer orderIndex,
        Integer supersetGroup,
        Integer targetSets,
        Integer targetRepsMin,
        Integer targetRepsMax,
        BigDecimal targetWeightKg,
        Integer targetWeightPercentage,
        Integer restSeconds,
        Integer restSecondsMax,
        Integer targetDurationSeconds,
        Integer targetDistanceMeters,
        Integer targetCalories,
        Integer targetRpe,
        Integer targetRpeMax,
        String tempo,
        String notes,
        List<String> formCues,
        Boolean isOptional,
        Boolean isWarmup,
        Boolean isCooldown,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PlanExerciseResponse from(WorkoutPlanExercise wpe) {
        return new PlanExerciseResponse(
                wpe.getId(),
                wpe.getWorkoutPlan().getId(),
                wpe.getExercise().getId(),
                wpe.getExercise().getName(),
                wpe.getExercise().getCategory(),
                wpe.getOrderIndex(),
                wpe.getSupersetGroup(),
                wpe.getTargetSets(),
                wpe.getTargetRepsMin(),
                wpe.getTargetRepsMax(),
                wpe.getTargetWeightKg(),
                wpe.getTargetWeightPercentage(),
                wpe.getRestSeconds(),
                wpe.getRestSecondsMax(),
                wpe.getTargetDurationSeconds(),
                wpe.getTargetDistanceMeters(),
                wpe.getTargetCalories(),
                wpe.getTargetRpe(),
                wpe.getTargetRpeMax(),
                wpe.getTempo(),
                wpe.getNotes(),
                wpe.getFormCues(),
                wpe.getIsOptional(),
                wpe.getIsWarmup(),
                wpe.getIsCooldown(),
                wpe.getCreatedAt(),
                wpe.getUpdatedAt()
        );
    }
}
