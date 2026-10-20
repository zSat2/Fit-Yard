package com.DingDong.Ding.Dong.Tracker.repository;

import com.DingDong.Ding.Dong.Tracker.entity.SessionSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionSetRepository extends JpaRepository<SessionSet, Long> {

    List<SessionSet> findBySessionExerciseIdOrderBySetNumber(Long sessionExerciseId);

    Optional<SessionSet> findByIdAndSessionExerciseId(Long id, Long sessionExerciseId);
}
