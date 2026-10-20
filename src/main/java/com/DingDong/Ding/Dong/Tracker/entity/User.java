package com.DingDong.Ding.Dong.Tracker.entity;

import com.DingDong.Ding.Dong.Tracker.enums.Gender;
import com.DingDong.Ding.Dong.Tracker.enums.PreferredUnits;
import com.DingDong.Ding.Dong.Tracker.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Auth / Identity
    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    // Basic Profile
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    // Preferences
    @Enumerated(EnumType.STRING)
    private PreferredUnits preferredUnits = PreferredUnits.METRIC;

    private String timezone = "UTC";

    // Account State
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    private Boolean isActive = true;
    private Boolean isEmailVerified = false;

    // Security
    private LocalDateTime lastLoginAt;
    private Integer failedLoginAttempts = 0;
    private LocalDateTime accountLockedUntil;
    private LocalDateTime passwordChangedAt;

    // Metadata
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}