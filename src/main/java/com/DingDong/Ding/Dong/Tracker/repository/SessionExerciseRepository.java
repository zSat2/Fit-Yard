package com.DingDong.Ding.Dong.Tracker.repository;

import com.DingDong.Ding.Dong.Tracker.entity.SessionExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionExerciseRepository extends JpaRepository<SessionExercise, Long> {

    List<SessionExercise> findByWorkoutSessionIdOrderByOrderIndex(Long sessionId);

    Optional<SessionExercise> findByIdAndWorkoutSessionId(Long id, Long sessionId);
}
