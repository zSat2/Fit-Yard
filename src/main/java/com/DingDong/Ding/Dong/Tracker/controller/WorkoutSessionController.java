package com.DingDong.Ding.Dong.Tracker.controller;

import com.DingDong.Ding.Dong.Tracker.dto.request.AddSessionExerciseRequest;
import com.DingDong.Ding.Dong.Tracker.dto.request.FinishSessionRequest;
import com.DingDong.Ding.Dong.Tracker.dto.request.LogSetRequest;
import com.DingDong.Ding.Dong.Tracker.dto.request.StartSessionRequest;
import com.DingDong.Ding.Dong.Tracker.dto.response.PageResponse;
import com.DingDong.Ding.Dong.Tracker.dto.response.SessionDetailResponse;
import com.DingDong.Ding.Dong.Tracker.dto.response.SessionExerciseResponse;
import com.DingDong.Ding.Dong.Tracker.dto.response.SessionSetResponse;
import com.DingDong.Ding.Dong.Tracker.dto.response.WorkoutSessionResponse;
import com.DingDong.Ding.Dong.Tracker.security.UserDetailsImpl;
import com.DingDong.Ding.Dong.Tracker.service.WorkoutSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class WorkoutSessionController {

    private final WorkoutSessionService workoutSessionService;

    @PostMapping
    public ResponseEntity<WorkoutSessionResponse> startSession(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @Valid @RequestBody StartSessionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workoutSessionService.startSession(principal.getId(), request));
    }

    @GetMapping
    public ResponseEntity<PageResponse<WorkoutSessionResponse>> getSessions(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(PageResponse.from(workoutSessionService.getSessions(principal.getId(), pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionDetailResponse> getSessionDetail(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @PathVariable Long id) {
        return ResponseEntity.ok(workoutSessionService.getSessionDetail(principal.getId(), id));
    }

    @PostMapping("/{id}/finish")
    public ResponseEntity<WorkoutSessionResponse> finishSession(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @PathVariable Long id,
            @Valid @RequestBody FinishSessionRequest request) {
        return ResponseEntity.ok(workoutSessionService.finishSession(principal.getId(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @PathVariable Long id) {
        workoutSessionService.deleteSession(principal.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/exercises")
    public ResponseEntity<SessionExerciseResponse> addExerciseToSession(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @PathVariable Long id,
            @Valid @RequestBody AddSessionExerciseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workoutSessionService.addExerciseToSession(principal.getId(), id, request));
    }

    @GetMapping("/{id}/exercises/{exId}/sets")
    public ResponseEntity<List<SessionSetResponse>> getSetsForExercise(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @PathVariable Long id,
            @PathVariable Long exId) {
        return ResponseEntity.ok(workoutSessionService.getSetsForExercise(principal.getId(), id, exId));
    }

    @PostMapping("/{id}/exercises/{exId}/sets")
    public ResponseEntity<SessionSetResponse> logSet(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @PathVariable Long id,
            @PathVariable Long exId,
            @Valid @RequestBody LogSetRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workoutSessionService.logSet(principal.getId(), id, exId, request));
    }
}
