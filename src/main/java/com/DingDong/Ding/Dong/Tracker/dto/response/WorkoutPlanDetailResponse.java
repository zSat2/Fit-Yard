package com.DingDong.Ding.Dong.Tracker.dto.response;

import com.DingDong.Ding.Dong.Tracker.entity.WorkoutPlan;
import com.DingDong.Ding.Dong.Tracker.entity.WorkoutPlanExercise;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record WorkoutPlanDetailResponse(
        Long id,
        Long userId,
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
        Boolean isTemplate,
        Boolean allowCopying,
        Integer timesUsed,
        Integer timesCompleted,
        BigDecimal averageRating,
        Integer totalRatings,
        Boolean isActive,
        Boolean isArchived,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<PlanExerciseResponse> exercises
) {
    public static WorkoutPlanDetailResponse from(WorkoutPlan p, List<WorkoutPlanExercise> exercises) {
        return new WorkoutPlanDetailResponse(
                p.getId(),
                p.getUser().getId(),
                p.getName(),
                p.getDescription(),
                p.getGoal(),
                p.getDifficultyLevel(),
                p.getProgramType(),
                p.getEstimatedDurationMinutes(),
                p.getTargetDaysPerWeek(),
                p.getEquipmentNeeded(),
                p.getLocation(),
                p.getIsPublic(),
                p.getIsTemplate(),
                p.getAllowCopying(),
                p.getTimesUsed(),
                p.getTimesCompleted(),
                p.getAverageRating(),
                p.getTotalRatings(),
                p.getIsActive(),
                p.getIsArchived(),
                p.getCreatedAt(),
                p.getUpdatedAt(),
                exercises.stream().map(PlanExerciseResponse::from).toList()
        );
    }
}
