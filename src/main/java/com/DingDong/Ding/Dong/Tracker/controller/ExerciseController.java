package com.DingDong.Ding.Dong.Tracker.controller;

import com.DingDong.Ding.Dong.Tracker.dto.request.CreateExerciseRequest;
import com.DingDong.Ding.Dong.Tracker.dto.response.ExerciseResponse;
import com.DingDong.Ding.Dong.Tracker.dto.response.PageResponse;
import com.DingDong.Ding.Dong.Tracker.security.UserDetailsImpl;
import com.DingDong.Ding.Dong.Tracker.service.ExerciseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    @GetMapping
    public ResponseEntity<PageResponse<ExerciseResponse>> listExercises(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String muscleGroup,
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(PageResponse.from(exerciseService.listExercises(category, muscleGroup, q, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseResponse> getExercise(@PathVariable Long id) {
        return ResponseEntity.ok(exerciseService.getExercise(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ExerciseResponse> getExerciseBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(exerciseService.getExerciseBySlug(slug));
    }

    @PostMapping
    public ResponseEntity<ExerciseResponse> createExercise(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @Valid @RequestBody CreateExerciseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(exerciseService.createExercise(principal.getId(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExerciseResponse> updateExercise(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @PathVariable Long id,
            @Valid @RequestBody CreateExerciseRequest request) {
        return ResponseEntity.ok(exerciseService.updateExercise(principal.getId(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @PathVariable Long id) {
        exerciseService.deleteExercise(principal.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
