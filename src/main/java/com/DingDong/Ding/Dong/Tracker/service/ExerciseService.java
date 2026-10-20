package com.DingDong.Ding.Dong.Tracker.service;

import com.DingDong.Ding.Dong.Tracker.dto.request.CreateExerciseRequest;
import com.DingDong.Ding.Dong.Tracker.dto.response.ExerciseResponse;
import com.DingDong.Ding.Dong.Tracker.entity.Exercise;
import com.DingDong.Ding.Dong.Tracker.enums.Role;
import com.DingDong.Ding.Dong.Tracker.exception.ConflictException;
import com.DingDong.Ding.Dong.Tracker.exception.ForbiddenException;
import com.DingDong.Ding.Dong.Tracker.exception.ResourceNotFoundException;
import com.DingDong.Ding.Dong.Tracker.repository.ExerciseRepository;
import com.DingDong.Ding.Dong.Tracker.repository.UserRepository;
import com.DingDong.Ding.Dong.Tracker.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<ExerciseResponse> listExercises(String category, String muscleGroup, String q, Pageable pageable) {
        if (StringUtils.hasText(q)) {
            return exerciseRepository.search(q, pageable).map(ExerciseResponse::from);
        }
        if (StringUtils.hasText(category)) {
            return exerciseRepository.findByCategoryAndIsActiveTrueAndDeletedAtIsNull(category.toUpperCase(), pageable)
                    .map(ExerciseResponse::from);
        }
        if (StringUtils.hasText(muscleGroup)) {
            return exerciseRepository.findByPrimaryMuscleGroupAndIsActiveTrueAndDeletedAtIsNull(muscleGroup.toUpperCase(), pageable)
                    .map(ExerciseResponse::from);
        }
        return exerciseRepository.findByIsActiveTrueAndDeletedAtIsNull(pageable).map(ExerciseResponse::from);
    }

    @Transactional(readOnly = true)
    public ExerciseResponse getExercise(Long id) {
        Exercise exercise = exerciseRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise", id));
        return ExerciseResponse.from(exercise);
    }

    @Transactional(readOnly = true)
    public ExerciseResponse getExerciseBySlug(String slug) {
        Exercise exercise = exerciseRepository.findBySlugAndDeletedAtIsNull(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found with slug: " + slug));
        return ExerciseResponse.from(exercise);
    }

    @Transactional
    public ExerciseResponse createExercise(Long userId, CreateExerciseRequest request) {
        if (exerciseRepository.existsByNameAndDeletedAtIsNull(request.name())) {
            throw new ConflictException("Exercise with name '" + request.name() + "' already exists");
        }

        String slug = generateUniqueSlug(request.name());

        Exercise exercise = new Exercise();
        mapRequestToExercise(exercise, request);
        exercise.setSlug(slug);
        exercise.setCreatedByUserId(userId);
        exercise.setIsCustom(true);
        exercise.setIsActive(true);
        exercise.setIsVerified(false);
        exercise.setTimesLogged(0);

        exercise = exerciseRepository.save(exercise);
        return ExerciseResponse.from(exercise);
    }

    @Transactional
    public ExerciseResponse updateExercise(Long userId, Long id, CreateExerciseRequest request) {
        Exercise exercise = exerciseRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise", id));

        // Only creator or ADMIN/TRAINER can update
        var user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        boolean isOwner = userId.equals(exercise.getCreatedByUserId());
        boolean isAdminOrTrainer = user.getRole() == Role.ADMIN || user.getRole() == Role.TRAINER;
        if (!isOwner && !isAdminOrTrainer) {
            throw new ForbiddenException("You do not have permission to update this exercise");
        }

        mapRequestToExercise(exercise, request);
        exercise = exerciseRepository.save(exercise);
        return ExerciseResponse.from(exercise);
    }

    @Transactional
    public void deleteExercise(Long userId, Long id) {
        Exercise exercise = exerciseRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise", id));

        var user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        boolean isOwner = userId.equals(exercise.getCreatedByUserId());
        boolean isAdmin = user.getRole() == Role.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new ForbiddenException("You do not have permission to delete this exercise");
        }

        exercise.setDeletedAt(java.time.LocalDateTime.now());
        exercise.setIsActive(false);
        exerciseRepository.save(exercise);
    }

    private String generateUniqueSlug(String name) {
        String baseSlug = ValidationUtil.toSlug(name);
        String slug = baseSlug;
        int counter = 2;
        while (exerciseRepository.existsBySlugAndDeletedAtIsNull(slug)) {
            slug = baseSlug + "-" + counter++;
        }
        return slug;
    }

    private void mapRequestToExercise(Exercise exercise, CreateExerciseRequest request) {
        exercise.setName(request.name());
        exercise.setDescription(request.description());
        exercise.setCategory(request.category());
        exercise.setPrimaryMuscleGroup(request.primaryMuscleGroup());
        exercise.setSecondaryMuscles(request.secondaryMuscles());
        exercise.setMovementPattern(request.movementPattern());
        exercise.setEquipment(request.equipment());
        exercise.setIsBodyweight(request.isBodyweight());
        exercise.setIsUnilateral(request.isUnilateral());
        exercise.setIsCompound(request.isCompound());
        exercise.setDifficultyLevel(request.difficultyLevel());
        exercise.setRequiresSpotter(request.requiresSpotter());
        exercise.setDefaultTrackingMode(request.defaultTrackingMode());
        exercise.setSupportsWeight(request.supportsWeight());
        exercise.setSupportsReps(request.supportsReps());
        exercise.setSupportsDuration(request.supportsDuration());
        exercise.setSupportsDistance(request.supportsDistance());
        exercise.setInjuryRisks(request.injuryRisks());
        exercise.setCoachingCues(request.coachingCues());
        exercise.setCommonMistakes(request.commonMistakes());
        exercise.setSetupInstructions(request.setupInstructions());
        exercise.setExecutionSteps(request.executionSteps());
        exercise.setVideoUrl(request.videoUrl());
        exercise.setThumbnailUrl(request.thumbnailUrl());
    }
}
