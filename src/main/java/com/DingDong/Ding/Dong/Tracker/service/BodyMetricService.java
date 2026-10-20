package com.DingDong.Ding.Dong.Tracker.service;

import com.DingDong.Ding.Dong.Tracker.dto.request.CreateBodyMetricRequest;
import com.DingDong.Ding.Dong.Tracker.dto.response.BodyMetricResponse;
import com.DingDong.Ding.Dong.Tracker.entity.User;
import com.DingDong.Ding.Dong.Tracker.entity.UserBodyMetric;
import com.DingDong.Ding.Dong.Tracker.exception.ForbiddenException;
import com.DingDong.Ding.Dong.Tracker.exception.ResourceNotFoundException;
import com.DingDong.Ding.Dong.Tracker.repository.BodyMetricRepository;
import com.DingDong.Ding.Dong.Tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class BodyMetricService {

    private final BodyMetricRepository bodyMetricRepository;
    private final UserRepository userRepository;

    @Transactional
    public BodyMetricResponse addMetric(Long userId, CreateBodyMetricRequest request) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        UserBodyMetric metric = new UserBodyMetric();
        metric.setUser(user);
        metric.setWeightKg(request.weightKg());
        metric.setBodyFatPct(request.bodyFatPct());
        metric.setHeightCm(request.heightCm());
        metric.setMeasurementTime(request.measurementTime());
        metric.setMeasurementMethod(request.measurementMethod());
        metric.setNotes(request.notes());
        metric.setRecordedAt(request.recordedAt());

        // Auto-calculate BMI if weight and height provided
        if (request.weightKg() != null && request.heightCm() != null
                && request.heightCm().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal heightM = request.heightCm().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
            BigDecimal bmi = request.weightKg().divide(heightM.multiply(heightM), 1, RoundingMode.HALF_UP);
            metric.setBmi(bmi);
        }

        metric = bodyMetricRepository.save(metric);
        return BodyMetricResponse.from(metric);
    }

    @Transactional(readOnly = true)
    public Page<BodyMetricResponse> getMetrics(Long userId, Pageable pageable) {
        return bodyMetricRepository.findByUserIdOrderByRecordedAtDesc(userId, pageable)
                .map(BodyMetricResponse::from);
    }

    @Transactional(readOnly = true)
    public BodyMetricResponse getLatestMetric(Long userId) {
        return bodyMetricRepository.findFirstByUserIdOrderByRecordedAtDesc(userId)
                .map(BodyMetricResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("No body metrics found for user"));
    }

    @Transactional
    public void deleteMetric(Long userId, Long metricId) {
        UserBodyMetric metric = bodyMetricRepository.findById(metricId)
                .orElseThrow(() -> new ResourceNotFoundException("Body metric", metricId));
        if (!metric.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You do not have permission to delete this metric");
        }
        bodyMetricRepository.delete(metric);
    }
}
