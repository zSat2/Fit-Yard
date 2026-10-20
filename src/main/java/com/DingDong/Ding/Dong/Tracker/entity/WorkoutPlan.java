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
@Table(name = "workout_plans")
@Getter
@Setter
@NoArgsConstructor
public class WorkoutPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 50)
    private String goal;

    @Column(name = "difficulty_level", length = 20)
    private String difficultyLevel;

    @Column(name = "program_type", length = 50)
    private String programType;

    @Column(name = "estimated_duration_minutes")
    private Integer estimatedDurationMinutes;

    @Column(name = "target_days_per_week")
    private Integer targetDaysPerWeek;

    @Convert(converter = StringArrayConverter.class)
    @Column(name = "equipment_needed", columnDefinition = "TEXT[]")
    private List<String> equipmentNeeded;

    @Column(length = 50)
    private String location;

    @Column(name = "is_public")
    private Boolean isPublic = false;

    @Column(name = "is_template")
    private Boolean isTemplate = false;

    @Column(name = "allow_copying")
    private Boolean allowCopying = true;

    @Column(name = "times_used")
    private Integer timesUsed = 0;

    @Column(name = "times_completed")
    private Integer timesCompleted = 0;

    @Column(name = "average_rating", precision = 3, scale = 2)
    private BigDecimal averageRating;

    @Column(name = "total_ratings")
    private Integer totalRatings = 0;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_archived")
    private Boolean isArchived = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
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
