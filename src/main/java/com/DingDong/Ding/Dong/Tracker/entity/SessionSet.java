package com.DingDong.Ding.Dong.Tracker.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "session_sets",
       uniqueConstraints = @UniqueConstraint(columnNames = {"session_exercise_id", "set_number"}))
@Getter
@Setter
@NoArgsConstructor
public class SessionSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_exercise_id", nullable = false)
    private SessionExercise sessionExercise;

    @Column(name = "set_number", nullable = false)
    private Integer setNumber;

    @Column(name = "weight_kg", precision = 6, scale = 2)
    private BigDecimal weightKg;

    @Column(name = "reps_completed")
    private Integer repsCompleted;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "distance_meters")
    private Integer distanceMeters;

    private Integer rpe;

    @Column(name = "is_completed")
    private Boolean isCompleted = true;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
