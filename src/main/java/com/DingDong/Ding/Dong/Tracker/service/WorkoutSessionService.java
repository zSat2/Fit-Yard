package com.DingDong.Ding.Dong.Tracker.service;

import com.DingDong.Ding.Dong.Tracker.dto.request.AddSessionExerciseRequest;
import com.DingDong.Ding.Dong.Tracker.dto.request.FinishSessionRequest;
import com.DingDong.Ding.Dong.Tracker.dto.request.LogSetRequest;
import com.DingDong.Ding.Dong.Tracker.dto.request.StartSessionRequest;
import com.DingDong.Ding.Dong.Tracker.dto.response.SessionDetailResponse;
import com.DingDong.Ding.Dong.Tracker.dto.response.SessionExerciseResponse;
import com.DingDong.Ding.Dong.Tracker.dto.response.SessionSetResponse;
import com.DingDong.Ding.Dong.Tracker.dto.response.StatsResponse;
import com.DingDong.Ding.Dong.Tracker.dto.response.WorkoutSessionResponse;
import com.DingDong.Ding.Dong.Tracker.entity.Exercise;
import com.DingDong.Ding.Dong.Tracker.entity.SessionExercise;
import com.DingDong.Ding.Dong.Tracker.entity.SessionSet;
import com.DingDong.Ding.Dong.Tracker.entity.User;
import com.DingDong.Ding.Dong.Tracker.entity.WorkoutPlan;
import com.DingDong.Ding.Dong.Tracker.entity.WorkoutSession;
import com.DingDong.Ding.Dong.Tracker.exception.BadRequestException;
import com.DingDong.Ding.Dong.Tracker.exception.ResourceNotFoundException;
import com.DingDong.Ding.Dong.Tracker.repository.ExerciseRepository;
import com.DingDong.Ding.Dong.Tracker.repository.SessionExerciseRepository;
import com.DingDong.Ding.Dong.Tracker.repository.SessionSetRepository;
import com.DingDong.Ding.Dong.Tracker.repository.UserRepository;
import com.DingDong.Ding.Dong.Tracker.repository.WorkoutPlanRepository;
import com.DingDong.Ding.Dong.Tracker.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WorkoutSessionService {

    private final WorkoutSessionRepository sessionRepository;
    private final SessionExerciseRepository sessionExerciseRepository;
    private final SessionSetRepository sessionSetRepository;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final UserRepository userRepository;

    @Transactional
    public WorkoutSessionResponse startSession(Long userId, StartSessionRequest request) {
        User user = findUserOrThrow(userId);

        WorkoutSession session = new WorkoutSession();
        session.setUser(user);
        session.setStartedAt(request.startedAt());
        session.setLocationType(request.locationType());
        session.setNotes(request.notes());

        if (request.workoutPlanId() != null) {
            WorkoutPlan plan = workoutPlanRepository.findByIdAndDeletedAtIsNull(request.workoutPlanId())
                    .orElseThrow(() -> new ResourceNotFoundException("WorkoutPlan", request.workoutPlanId()));
            session.setWorkoutPlan(plan);
        }

        session = sessionRepository.save(session);
        return WorkoutSessionResponse.from(session);
    }

    @Transactional
    public WorkoutSessionResponse finishSession(Long userId, Long sessionId, FinishSessionRequest request) {
        WorkoutSession session = findOwnedSessionOrThrow(userId, sessionId);

        if (request.endedAt().isBefore(session.getStartedAt())) {
            throw new BadRequestException("End time cannot be before start time");
        }

        session.setEndedAt(request.endedAt());
        session.setPerceivedExertion(request.perceivedExertion());
        if (request.notes() != null) session.setNotes(request.notes());

        session = sessionRepository.save(session);
        return WorkoutSessionResponse.from(session);
    }

    @Transactional(readOnly = true)
    public Page<WorkoutSessionResponse> getSessions(Long userId, Pageable pageable) {
        return sessionRepository.findByUserIdOrderByStartedAtDesc(userId, pageable)
                .map(WorkoutSessionResponse::from);
    }

    @Transactional(readOnly = true)
    public SessionDetailResponse getSessionDetail(Long userId, Long sessionId) {
        WorkoutSession session = findOwnedSessionOrThrow(userId, sessionId);
        List<SessionExercise> exercises = sessionExerciseRepository.findByWorkoutSessionIdOrderByOrderIndex(sessionId);
        return SessionDetailResponse.from(session, exercises);
    }

    @Transactional
    public void deleteSession(Long userId, Long sessionId) {
        WorkoutSession session = findOwnedSessionOrThrow(userId, sessionId);
        sessionRepository.delete(session);
    }

    @Transactional
    public SessionExerciseResponse addExerciseToSession(Long userId, Long sessionId, AddSessionExerciseRequest request) {
        WorkoutSession session = findOwnedSessionOrThrow(userId, sessionId);
        Exercise exercise = exerciseRepository.findByIdAndDeletedAtIsNull(request.exerciseId())
                .orElseThrow(() -> new ResourceNotFoundException("Exercise", request.exerciseId()));

        SessionExercise se = new SessionExercise();
        se.setWorkoutSession(session);
        se.setExercise(exercise);
        se.setOrderIndex(request.orderIndex());
        se.setSupersetGroup(request.supersetGroup());
        se.setWasPlanned(request.wasPlanned());
        se.setNotes(request.notes());

        se = sessionExerciseRepository.save(se);
        return SessionExerciseResponse.from(se);
    }

    @Transactional
    public SessionSetResponse logSet(Long userId, Long sessionId, Long sessionExerciseId, LogSetRequest request) {
        findOwnedSessionOrThrow(userId, sessionId);

        SessionExercise se = sessionExerciseRepository.findByIdAndWorkoutSessionId(sessionExerciseId, sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("SessionExercise", sessionExerciseId));

        // Validate at least one of weight/duration/distance is non-null
        if (request.weightKg() == null && request.durationSeconds() == null && request.distanceMeters() == null) {
            throw new BadRequestException("At least one of weightKg, durationSeconds, or distanceMeters must be provided");
        }

        SessionSet set = new SessionSet();
        set.setSessionExercise(se);
        set.setSetNumber(request.setNumber());
        set.setWeightKg(request.weightKg());
        set.setRepsCompleted(request.repsCompleted());
        set.setDurationSeconds(request.durationSeconds());
        set.setDistanceMeters(request.distanceMeters());
        set.setRpe(request.rpe());
        set.setIsCompleted(request.isCompleted() != null ? request.isCompleted() : true);
        set.setNotes(request.notes());

        set = sessionSetRepository.save(set);
        return SessionSetResponse.from(set);
    }

    @Transactional(readOnly = true)
    public List<SessionSetResponse> getSetsForExercise(Long userId, Long sessionId, Long sessionExerciseId) {
        findOwnedSessionOrThrow(userId, sessionId);
        return sessionSetRepository.findBySessionExerciseIdOrderBySetNumber(sessionExerciseId)
                .stream().map(SessionSetResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public StatsResponse getStats(Long userId) {
        long totalSessions = sessionRepository.countByUserId(userId);

        // This week sessions
        LocalDateTime weekStart = LocalDateTime.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .toLocalDate().atStartOfDay();
        LocalDateTime weekEnd = LocalDateTime.now();
        long thisWeekSessions = sessionRepository.findByUserIdAndStartedAtBetweenOrderByStartedAtDesc(
                userId, weekStart, weekEnd).size();

        // Compute total volume and favorite exercise
        List<WorkoutSession> allSessions = sessionRepository.findByUserIdAndStartedAtBetweenOrderByStartedAtDesc(
                userId, LocalDateTime.of(2000, 1, 1, 0, 0), LocalDateTime.now());

        double totalVolumeKg = 0.0;
        long totalDurationSeconds = 0L;
        Map<String, Integer> exerciseCounts = new HashMap<>();

        for (WorkoutSession session : allSessions) {
            List<SessionExercise> exercises = sessionExerciseRepository.findByWorkoutSessionIdOrderByOrderIndex(session.getId());
            for (SessionExercise ex : exercises) {
                String exerciseName = ex.getExercise().getName();
                exerciseCounts.merge(exerciseName, 1, Integer::sum);

                List<SessionSet> sets = sessionSetRepository.findBySessionExerciseIdOrderBySetNumber(ex.getId());
                for (SessionSet set : sets) {
                    if (set.getWeightKg() != null && set.getRepsCompleted() != null) {
                        totalVolumeKg += set.getWeightKg().doubleValue() * set.getRepsCompleted();
                    }
                    if (set.getDurationSeconds() != null) {
                        totalDurationSeconds += set.getDurationSeconds();
                    }
                }
            }
        }

        String favoriteExercise = exerciseCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        double totalVolumeTonnes = totalVolumeKg / 1000.0;
        long totalDurationMinutes = totalDurationSeconds / 60;

        return new StatsResponse(totalSessions, totalVolumeTonnes, totalDurationMinutes, favoriteExercise, thisWeekSessions);
    }

    private WorkoutSession findOwnedSessionOrThrow(Long userId, Long sessionId) {
        return sessionRepository.findByIdAndUserId(sessionId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkoutSession", sessionId));
    }

    private User findUserOrThrow(Long userId) {
        return userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }
}
