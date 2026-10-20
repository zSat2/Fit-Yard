package com.DingDong.Ding.Dong.Tracker.dto.response;

import com.DingDong.Ding.Dong.Tracker.entity.SessionSet;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SessionSetResponse(
        Long id,
        Long sessionExerciseId,
        Integer setNumber,
        BigDecimal weightKg,
        Integer repsCompleted,
        Integer durationSeconds,
        Integer distanceMeters,
        Integer rpe,
        Boolean isCompleted,
        String notes,
        LocalDateTime createdAt
) {
    public static SessionSetResponse from(SessionSet ss) {
        return new SessionSetResponse(
                ss.getId(),
                ss.getSessionExercise().getId(),
                ss.getSetNumber(),
                ss.getWeightKg(),
                ss.getRepsCompleted(),
                ss.getDurationSeconds(),
                ss.getDistanceMeters(),
                ss.getRpe(),
                ss.getIsCompleted(),
                ss.getNotes(),
                ss.getCreatedAt()
        );
    }
}
