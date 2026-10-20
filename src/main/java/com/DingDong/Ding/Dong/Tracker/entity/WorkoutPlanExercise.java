package com.DingDong.Ding.Dong.Tracker.entity;

import com.DingDong.Ding.Dong.Tracker.util.StringArrayConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "workout_plan_exercises",
       uniqueConstraints = @UniqueConstraint(columnNames = {"workout_plan_id", "order_index"}))
@Getter
@Setter
@NoArgsConstructor
public class WorkoutPlanExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_plan_id", nullable = false)
    private WorkoutPlan workoutPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @Column(name = "superset_group")
    private Integer supersetGroup;

    @Column(name = "target_sets")
    private Integer targetSets;

    @Column(name = "target_reps_min")
    private Integer targetRepsMin;

    @Column(name = "target_reps_max")
    private Integer targetRepsMax;

    @Column(name = "target_weight_kg", precision = 6, scale = 2)
    private BigDecimal targetWeightKg;

    @Column(name = "target_weight_percentage")
    private Integer targetWeightPercentage;

    @Column(name = "rest_seconds")
    private Integer restSeconds = 60;

    @Column(name = "rest_seconds_max")
    private Integer restSecondsMax;

    @Column(name = "target_duration_seconds")
    private Integer targetDurationSeconds;

    @Column(name = "target_distance_meters")
    private Integer targetDistanceMeters;

    @Column(name = "target_calories")
    private Integer targetCalories;

    @Column(name = "target_rpe")
    private Integer targetRpe;

    @Column(name = "target_rpe_max")
    private Integer targetRpeMax;

    @Column(length = 20)
    private String tempo;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Convert(converter = StringArrayConverter.class)
    @Column(name = "form_cues", columnDefinition = "TEXT[]")
    private List<String> formCues;

    @Column(name = "is_optional")
    private Boolean isOptional = false;

    @Column(name = "is_warmup")
    private Boolean isWarmup = false;

    @Column(name = "is_cooldown")
    private Boolean isCooldown = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

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
