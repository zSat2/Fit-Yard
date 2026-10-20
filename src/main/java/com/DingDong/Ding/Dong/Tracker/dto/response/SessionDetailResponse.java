package com.DingDong.Ding.Dong.Tracker.dto.response;

import com.DingDong.Ding.Dong.Tracker.entity.WorkoutSession;
import com.DingDong.Ding.Dong.Tracker.entity.SessionExercise;

import java.time.LocalDateTime;
import java.util.List;

public record SessionDetailResponse(
        Long id,
        Long userId,
        Long workoutPlanId,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        String locationType,
        Integer perceivedExertion,
        String notes,
        LocalDateTime createdAt,
        List<SessionExerciseResponse> exercises
) {
    public static SessionDetailResponse from(WorkoutSession s, List<SessionExercise> exercises) {
        return new SessionDetailResponse(
                s.getId(),
                s.getUser().getId(),
                s.getWorkoutPlan() != null ? s.getWorkoutPlan().getId() : null,
                s.getStartedAt(),
                s.getEndedAt(),
                s.getLocationType(),
                s.getPerceivedExertion(),
                s.getNotes(),
                s.getCreatedAt(),
                exercises.stream().map(SessionExerciseResponse::from).toList()
        );
    }
}
