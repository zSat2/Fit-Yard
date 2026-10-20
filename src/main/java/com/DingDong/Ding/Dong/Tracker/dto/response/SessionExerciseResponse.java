package com.DingDong.Ding.Dong.Tracker.dto.response;

import com.DingDong.Ding.Dong.Tracker.entity.SessionExercise;

import java.time.LocalDateTime;

public record SessionExerciseResponse(
        Long id,
        Long workoutSessionId,
        Long exerciseId,
        String exerciseName,
        Integer orderIndex,
        Integer supersetGroup,
        Boolean wasPlanned,
        String notes,
        LocalDateTime createdAt
) {
    public static SessionExerciseResponse from(SessionExercise se) {
        return new SessionExerciseResponse(
                se.getId(),
                se.getWorkoutSession().getId(),
                se.getExercise().getId(),
                se.getExercise().getName(),
                se.getOrderIndex(),
                se.getSupersetGroup(),
                se.getWasPlanned(),
                se.getNotes(),
                se.getCreatedAt()
        );
    }
}
