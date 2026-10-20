package com.DingDong.Ding.Dong.Tracker.dto.request;

public record UpdateProfileRequest(
        String firstName,
        String lastName,
        String gender,
        String timezone,
        String preferredUnits
) {}
