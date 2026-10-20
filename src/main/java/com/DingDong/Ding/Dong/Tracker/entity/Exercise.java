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
@Table(name = "exercises")
@Getter
@Setter
@NoArgsConstructor
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 150)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 50)
    private String category;

    @Column(name = "primary_muscle_group", length = 50)
    private String primaryMuscleGroup;

    @Convert(converter = StringArrayConverter.class)
    @Column(name = "secondary_muscles", columnDefinition = "TEXT[]")
    private List<String> secondaryMuscles;

    @Column(name = "movement_pattern", length = 50)
    private String movementPattern;

    @Column(length = 100)
    private String equipment;

    @Column(name = "is_bodyweight")
    private Boolean isBodyweight;

    @Column(name = "is_unilateral")
    private Boolean isUnilateral;

    @Column(name = "is_compound")
    private Boolean isCompound;

    @Column(name = "difficulty_level")
    private String difficultyLevel;

    @Column(name = "requires_spotter")
    private Boolean requiresSpotter;

    @Column(name = "default_tracking_mode", length = 30)
    private String defaultTrackingMode;

    @Column(name = "supports_weight")
    private Boolean supportsWeight;

    @Column(name = "supports_reps")
    private Boolean supportsReps;

    @Column(name = "supports_duration")
    private Boolean supportsDuration;

    @Column(name = "supports_distance")
    private Boolean supportsDistance;

    @Convert(converter = StringArrayConverter.class)
    @Column(name = "injury_risks", columnDefinition = "TEXT[]")
    private List<String> injuryRisks;

    @Convert(converter = StringArrayConverter.class)
    @Column(name = "coaching_cues", columnDefinition = "TEXT[]")
    private List<String> coachingCues;

    @Convert(converter = StringArrayConverter.class)
    @Column(name = "common_mistakes", columnDefinition = "TEXT[]")
    private List<String> commonMistakes;

    @Column(name = "setup_instructions", columnDefinition = "TEXT")
    private String setupInstructions;

    @Convert(converter = StringArrayConverter.class)
    @Column(name = "execution_steps", columnDefinition = "TEXT[]")
    private List<String> executionSteps;

    @Column(name = "video_url", length = 500)
    private String videoUrl;

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    @Column(name = "demonstration_gif_url", length = 500)
    private String demonstrationGifUrl;

    @Column(name = "is_custom")
    private Boolean isCustom;

    @Column(name = "created_by_user_id")
    private Long createdByUserId;

    @Column(name = "times_logged")
    private Integer timesLogged;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_verified")
    private Boolean isVerified;

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
