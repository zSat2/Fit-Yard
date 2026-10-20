package com.DingDong.Ding.Dong.Tracker.dto.response;

public record StatsResponse(
        long totalSessions,
        Double totalVolumeTonnes,
        Long totalDurationMinutes,
        String favoriteExercise,
        long thisWeekSessions
) {}
