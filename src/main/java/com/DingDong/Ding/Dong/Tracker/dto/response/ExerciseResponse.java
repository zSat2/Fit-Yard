package com.DingDong.Ding.Dong.Tracker.dto.response;

import com.DingDong.Ding.Dong.Tracker.entity.Exercise;

import java.time.LocalDateTime;
import java.util.List;

public record ExerciseResponse(
        Long id,
        String name,
        String slug,
        String description,
        String category,
        String primaryMuscleGroup,
        List<String> secondaryMuscles,
        String movementPattern,
        String equipment,
        Boolean isBodyweight,
        Boolean isUnilateral,
        Boolean isCompound,
        String difficultyLevel,
        Boolean requiresSpotter,
        String defaultTrackingMode,
        Boolean supportsWeight,
        Boolean supportsReps,
        Boolean supportsDuration,
        Boolean supportsDistance,
        List<String> injuryRisks,
        List<String> coachingCues,
        List<String> commonMistakes,
        String setupInstructions,
        List<String> executionSteps,
        String videoUrl,
        String thumbnailUrl,
        String demonstrationGifUrl,
        Boolean isCustom,
        Long createdByUserId,
        Integer timesLogged,
        Boolean isActive,
        Boolean isVerified,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ExerciseResponse from(Exercise e) {
        return new ExerciseResponse(
                e.getId(),
                e.getName(),
                e.getSlug(),
                e.getDescription(),
                e.getCategory(),
                e.getPrimaryMuscleGroup(),
                e.getSecondaryMuscles(),
                e.getMovementPattern(),
                e.getEquipment(),
                e.getIsBodyweight(),
                e.getIsUnilateral(),
                e.getIsCompound(),
                e.getDifficultyLevel(),
                e.getRequiresSpotter(),
                e.getDefaultTrackingMode(),
                e.getSupportsWeight(),
                e.getSupportsReps(),
                e.getSupportsDuration(),
                e.getSupportsDistance(),
                e.getInjuryRisks(),
                e.getCoachingCues(),
                e.getCommonMistakes(),
                e.getSetupInstructions(),
                e.getExecutionSteps(),
                e.getVideoUrl(),
                e.getThumbnailUrl(),
                e.getDemonstrationGifUrl(),
                e.getIsCustom(),
                e.getCreatedByUserId(),
                e.getTimesLogged(),
                e.getIsActive(),
                e.getIsVerified(),
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }
}
