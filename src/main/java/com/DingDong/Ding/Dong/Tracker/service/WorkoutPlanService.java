package com.DingDong.Ding.Dong.Tracker.service;

import com.DingDong.Ding.Dong.Tracker.dto.request.AddPlanExerciseRequest;
import com.DingDong.Ding.Dong.Tracker.dto.request.CreateWorkoutPlanRequest;
import com.DingDong.Ding.Dong.Tracker.dto.response.PlanExerciseResponse;
import com.DingDong.Ding.Dong.Tracker.dto.response.WorkoutPlanDetailResponse;
import com.DingDong.Ding.Dong.Tracker.dto.response.WorkoutPlanResponse;
import com.DingDong.Ding.Dong.Tracker.entity.Exercise;
import com.DingDong.Ding.Dong.Tracker.entity.User;
import com.DingDong.Ding.Dong.Tracker.entity.WorkoutPlan;
import com.DingDong.Ding.Dong.Tracker.entity.WorkoutPlanExercise;
import com.DingDong.Ding.Dong.Tracker.exception.ForbiddenException;
import com.DingDong.Ding.Dong.Tracker.exception.ResourceNotFoundException;
import com.DingDong.Ding.Dong.Tracker.repository.ExerciseRepository;
import com.DingDong.Ding.Dong.Tracker.repository.UserRepository;
import com.DingDong.Ding.Dong.Tracker.repository.WorkoutPlanExerciseRepository;
import com.DingDong.Ding.Dong.Tracker.repository.WorkoutPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutPlanService {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final WorkoutPlanExerciseRepository workoutPlanExerciseRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    @Transactional
    public WorkoutPlanResponse createPlan(Long userId, CreateWorkoutPlanRequest request) {
        User user = findUserOrThrow(userId);
        WorkoutPlan plan = new WorkoutPlan();
        mapRequestToPlan(plan, request);
        plan.setUser(user);
        plan = workoutPlanRepository.save(plan);
        return WorkoutPlanResponse.from(plan);
    }

    @Transactional(readOnly = true)
    public Page<WorkoutPlanResponse> getMyPlans(Long userId, Pageable pageable) {
        return workoutPlanRepository.findByUserIdAndDeletedAtIsNull(userId, pageable)
                .map(WorkoutPlanResponse::from);
    }

    @Transactional(readOnly = true)
    public Page<WorkoutPlanResponse> getPublicPlans(Pageable pageable) {
        return workoutPlanRepository.findByIsPublicTrueAndDeletedAtIsNull(pageable)
                .map(WorkoutPlanResponse::from);
    }

    @Transactional(readOnly = true)
    public WorkoutPlanDetailResponse getPlanDetail(Long userId, Long planId) {
        WorkoutPlan plan = workoutPlanRepository.findByIdAndDeletedAtIsNull(planId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkoutPlan", planId));

        // Access check: must be owner OR plan is public
        if (!plan.getUser().getId().equals(userId) && !Boolean.TRUE.equals(plan.getIsPublic())) {
            throw new ForbiddenException("You do not have permission to view this plan");
        }

        List<WorkoutPlanExercise> exercises = workoutPlanExerciseRepository.findByWorkoutPlanIdOrderByOrderIndex(planId);
        return WorkoutPlanDetailResponse.from(plan, exercises);
    }

    @Transactional
    public WorkoutPlanResponse updatePlan(Long userId, Long planId, CreateWorkoutPlanRequest request) {
        WorkoutPlan plan = findOwnedPlanOrThrow(userId, planId);
        mapRequestToPlan(plan, request);
        plan = workoutPlanRepository.save(plan);
        return WorkoutPlanResponse.from(plan);
    }

    @Transactional
    public void deletePlan(Long userId, Long planId) {
        WorkoutPlan plan = findOwnedPlanOrThrow(userId, planId);
        plan.setDeletedAt(LocalDateTime.now());
        plan.setIsActive(false);
        workoutPlanRepository.save(plan);
    }

    @Transactional
    public PlanExerciseResponse addExerciseToPlan(Long userId, Long planId, AddPlanExerciseRequest request) {
        WorkoutPlan plan = findOwnedPlanOrThrow(userId, planId);
        Exercise exercise = exerciseRepository.findByIdAndDeletedAtIsNull(request.exerciseId())
                .orElseThrow(() -> new ResourceNotFoundException("Exercise", request.exerciseId()));

        WorkoutPlanExercise wpe = new WorkoutPlanExercise();
        wpe.setWorkoutPlan(plan);
        wpe.setExercise(exercise);
        wpe.setOrderIndex(request.orderIndex());
        wpe.setSupersetGroup(request.supersetGroup());
        wpe.setTargetSets(request.targetSets());
        wpe.setTargetRepsMin(request.targetRepsMin());
        wpe.setTargetRepsMax(request.targetRepsMax());
        wpe.setTargetWeightKg(request.targetWeightKg());
        wpe.setRestSeconds(request.restSeconds() != null ? request.restSeconds() : 60);
        wpe.setRestSecondsMax(request.restSecondsMax());
        wpe.setTargetDurationSeconds(request.targetDurationSeconds());
        wpe.setTargetDistanceMeters(request.targetDistanceMeters());
        wpe.setTargetCalories(request.targetCalories());
        wpe.setTargetRpe(request.targetRpe());
        wpe.setTempo(request.tempo());
        wpe.setNotes(request.notes());
        wpe.setFormCues(request.formCues());
        wpe.setIsOptional(request.isOptional() != null ? request.isOptional() : false);
        wpe.setIsWarmup(request.isWarmup() != null ? request.isWarmup() : false);
        wpe.setIsCooldown(request.isCooldown() != null ? request.isCooldown() : false);

        wpe = workoutPlanExerciseRepository.save(wpe);
        return PlanExerciseResponse.from(wpe);
    }

    @Transactional
    public void removeExerciseFromPlan(Long userId, Long planId, Long wpeId) {
        findOwnedPlanOrThrow(userId, planId);
        WorkoutPlanExercise wpe = workoutPlanExerciseRepository.findByWorkoutPlanIdAndId(planId, wpeId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkoutPlanExercise", wpeId));
        workoutPlanExerciseRepository.delete(wpe);
    }

    @Transactional
    public void reorderExercises(Long userId, Long planId, List<Long> orderedIds) {
        findOwnedPlanOrThrow(userId, planId);
        List<WorkoutPlanExercise> exercises = workoutPlanExerciseRepository.findByWorkoutPlanIdOrderByOrderIndex(planId);
        for (int i = 0; i < orderedIds.size(); i++) {
            final int index = i;
            final Long wpeId = orderedIds.get(i);
            exercises.stream()
                    .filter(e -> e.getId().equals(wpeId))
                    .findFirst()
                    .ifPresent(e -> e.setOrderIndex(index + 1));
        }
        workoutPlanExerciseRepository.saveAll(exercises);
    }

    private WorkoutPlan findOwnedPlanOrThrow(Long userId, Long planId) {
        return workoutPlanRepository.findByIdAndUserIdAndDeletedAtIsNull(planId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkoutPlan", planId));
    }

    private User findUserOrThrow(Long userId) {
        return userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }

    private void mapRequestToPlan(WorkoutPlan plan, CreateWorkoutPlanRequest request) {
        plan.setName(request.name());
        plan.setDescription(request.description());
        plan.setGoal(request.goal());
        plan.setDifficultyLevel(request.difficultyLevel());
        plan.setProgramType(request.programType());
        plan.setEstimatedDurationMinutes(request.estimatedDurationMinutes());
        plan.setTargetDaysPerWeek(request.targetDaysPerWeek());
        plan.setEquipmentNeeded(request.equipmentNeeded());
        plan.setLocation(request.location());
        plan.setIsPublic(request.isPublic() != null ? request.isPublic() : false);
        plan.setIsTemplate(request.isTemplate() != null ? request.isTemplate() : false);
    }
}
