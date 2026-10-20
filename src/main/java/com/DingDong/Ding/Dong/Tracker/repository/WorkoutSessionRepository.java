package com.DingDong.Ding.Dong.Tracker.repository;

import com.DingDong.Ding.Dong.Tracker.entity.WorkoutSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Long> {

    Page<WorkoutSession> findByUserIdOrderByStartedAtDesc(Long userId, Pageable pageable);

    Optional<WorkoutSession> findByIdAndUserId(Long id, Long userId);

    List<WorkoutSession> findByUserIdAndStartedAtBetweenOrderByStartedAtDesc(Long userId, LocalDateTime start, LocalDateTime end);

    long countByUserId(Long userId);
}
