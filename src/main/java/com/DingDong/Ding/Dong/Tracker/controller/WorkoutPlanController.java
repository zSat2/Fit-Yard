package com.DingDong.Ding.Dong.Tracker.controller;

import com.DingDong.Ding.Dong.Tracker.dto.request.AddPlanExerciseRequest;
import com.DingDong.Ding.Dong.Tracker.dto.request.CreateWorkoutPlanRequest;
import com.DingDong.Ding.Dong.Tracker.dto.response.PageResponse;
import com.DingDong.Ding.Dong.Tracker.dto.response.PlanExerciseResponse;
import com.DingDong.Ding.Dong.Tracker.dto.response.WorkoutPlanDetailResponse;
import com.DingDong.Ding.Dong.Tracker.dto.response.WorkoutPlanResponse;
import com.DingDong.Ding.Dong.Tracker.security.UserDetailsImpl;
import com.DingDong.Ding.Dong.Tracker.service.WorkoutPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;

    @GetMapping("/my")
    public ResponseEntity<PageResponse<WorkoutPlanResponse>> getMyPlans(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(PageResponse.from(workoutPlanService.getMyPlans(principal.getId(), pageable)));
    }

    @GetMapping("/public")
    public ResponseEntity<PageResponse<WorkoutPlanResponse>> getPublicPlans(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(PageResponse.from(workoutPlanService.getPublicPlans(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutPlanDetailResponse> getPlanDetail(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @PathVariable Long id) {
        return ResponseEntity.ok(workoutPlanService.getPlanDetail(principal.getId(), id));
    }

    @PostMapping
    public ResponseEntity<WorkoutPlanResponse> createPlan(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @Valid @RequestBody CreateWorkoutPlanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workoutPlanService.createPlan(principal.getId(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutPlanResponse> updatePlan(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @PathVariable Long id,
            @Valid @RequestBody CreateWorkoutPlanRequest request) {
        return ResponseEntity.ok(workoutPlanService.updatePlan(principal.getId(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @PathVariable Long id) {
        workoutPlanService.deletePlan(principal.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/exercises")
    public ResponseEntity<PlanExerciseResponse> addExerciseToPlan(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @PathVariable Long id,
            @Valid @RequestBody AddPlanExerciseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workoutPlanService.addExerciseToPlan(principal.getId(), id, request));
    }

    @DeleteMapping("/{id}/exercises/{wpeId}")
    public ResponseEntity<Void> removeExerciseFromPlan(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @PathVariable Long id,
            @PathVariable Long wpeId) {
        workoutPlanService.removeExerciseFromPlan(principal.getId(), id, wpeId);
        return ResponseEntity.noContent().build();
    }
}
