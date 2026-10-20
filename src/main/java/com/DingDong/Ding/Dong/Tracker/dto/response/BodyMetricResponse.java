package com.DingDong.Ding.Dong.Tracker.dto.response;

import com.DingDong.Ding.Dong.Tracker.entity.UserBodyMetric;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BodyMetricResponse(
        Long id,
        Long userId,
        BigDecimal weightKg,
        BigDecimal bodyFatPct,
        BigDecimal heightCm,
        BigDecimal bmi,
        String measurementTime,
        String measurementMethod,
        String notes,
        LocalDateTime recordedAt,
        LocalDateTime createdAt
) {
    public static BodyMetricResponse from(UserBodyMetric m) {
        return new BodyMetricResponse(
                m.getId(),
                m.getUser().getId(),
                m.getWeightKg(),
                m.getBodyFatPct(),
                m.getHeightCm(),
                m.getBmi(),
                m.getMeasurementTime(),
                m.getMeasurementMethod(),
                m.getNotes(),
                m.getRecordedAt(),
                m.getCreatedAt()
        );
    }
}
