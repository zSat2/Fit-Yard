package com.DingDong.Ding.Dong.Tracker.dto.response;

import com.DingDong.Ding.Dong.Tracker.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String gender,
        String preferredUnits,
        String timezone,
        String role,
        LocalDateTime createdAt
) {
    public static UserResponse from(User u) {
        return new UserResponse(
                u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getFirstName(),
                u.getLastName(),
                u.getDateOfBirth(),
                u.getGender() != null ? u.getGender().name() : null,
                u.getPreferredUnits() != null ? u.getPreferredUnits().name() : null,
                u.getTimezone(),
                u.getRole() != null ? u.getRole().name() : null,
                u.getCreatedAt()
        );
    }
}
