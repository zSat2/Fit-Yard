package com.DingDong.Ding.Dong.Tracker.controller;

import com.DingDong.Ding.Dong.Tracker.dto.request.ChangePasswordRequest;
import com.DingDong.Ding.Dong.Tracker.dto.request.CreateBodyMetricRequest;
import com.DingDong.Ding.Dong.Tracker.dto.request.UpdateProfileRequest;
import com.DingDong.Ding.Dong.Tracker.dto.response.BodyMetricResponse;
import com.DingDong.Ding.Dong.Tracker.dto.response.PageResponse;
import com.DingDong.Ding.Dong.Tracker.dto.response.StatsResponse;
import com.DingDong.Ding.Dong.Tracker.dto.response.UserResponse;
import com.DingDong.Ding.Dong.Tracker.security.UserDetailsImpl;
import com.DingDong.Ding.Dong.Tracker.service.BodyMetricService;
import com.DingDong.Ding.Dong.Tracker.service.UserService;
import com.DingDong.Ding.Dong.Tracker.service.WorkoutSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final BodyMetricService bodyMetricService;
    private final WorkoutSessionService workoutSessionService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getProfile(@AuthenticationPrincipal UserDetailsImpl principal) {
        return ResponseEntity.ok(userService.getProfile(principal.getId()));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateProfile(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(userService.updateProfile(principal.getId(), request));
    }

    @PostMapping("/me/change-password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(principal.getId(), request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteAccount(@AuthenticationPrincipal UserDetailsImpl principal) {
        userService.deleteAccount(principal.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/body-metrics")
    public ResponseEntity<PageResponse<BodyMetricResponse>> getBodyMetrics(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(PageResponse.from(bodyMetricService.getMetrics(principal.getId(), pageable)));
    }

    @GetMapping("/me/body-metrics/latest")
    public ResponseEntity<BodyMetricResponse> getLatestBodyMetric(@AuthenticationPrincipal UserDetailsImpl principal) {
        return ResponseEntity.ok(bodyMetricService.getLatestMetric(principal.getId()));
    }

    @PostMapping("/me/body-metrics")
    public ResponseEntity<BodyMetricResponse> addBodyMetric(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @Valid @RequestBody CreateBodyMetricRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bodyMetricService.addMetric(principal.getId(), request));
    }

    @DeleteMapping("/me/body-metrics/{id}")
    public ResponseEntity<Void> deleteBodyMetric(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @PathVariable Long id) {
        bodyMetricService.deleteMetric(principal.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/stats")
    public ResponseEntity<StatsResponse> getStats(@AuthenticationPrincipal UserDetailsImpl principal) {
        return ResponseEntity.ok(workoutSessionService.getStats(principal.getId()));
    }
}
