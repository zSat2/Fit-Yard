package com.DingDong.Ding.Dong.Tracker.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record FinishSessionRequest(
        @NotNull(message = "Ended at timestamp is required")
        LocalDateTime endedAt,

        @Min(value = 1, message = "Perceived exertion must be between 1 and 10")
        @Max(value = 10, message = "Perceived exertion must be between 1 and 10")
        Integer perceivedExertion,

        String notes
) {}
