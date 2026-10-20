package com.DingDong.Ding.Dong.Tracker.dto.response;

import com.DingDong.Ding.Dong.Tracker.entity.WorkoutPlan;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record WorkoutPlanResponse(
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
        LocalDateTime updatedAt
) {
    public static WorkoutPlanResponse from(WorkoutPlan p) {
        return new WorkoutPlanResponse(
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
                p.getUpdatedAt()
        );
    }
}
