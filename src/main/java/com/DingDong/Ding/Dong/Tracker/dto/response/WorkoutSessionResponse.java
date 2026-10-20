package com.DingDong.Ding.Dong.Tracker.dto.response;

import com.DingDong.Ding.Dong.Tracker.entity.WorkoutSession;

import java.time.LocalDateTime;

public record WorkoutSessionResponse(
        Long id,
        Long userId,
        Long workoutPlanId,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        String locationType,
        Integer perceivedExertion,
        String notes,
        LocalDateTime createdAt
) {
    public static WorkoutSessionResponse from(WorkoutSession s) {
        return new WorkoutSessionResponse(
                s.getId(),
                s.getUser().getId(),
                s.getWorkoutPlan() != null ? s.getWorkoutPlan().getId() : null,
                s.getStartedAt(),
                s.getEndedAt(),
                s.getLocationType(),
                s.getPerceivedExertion(),
                s.getNotes(),
                s.getCreatedAt()
        );
    }
}
