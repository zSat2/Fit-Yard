package com.DingDong.Ding.Dong.Tracker.repository;

import com.DingDong.Ding.Dong.Tracker.entity.WorkoutPlanExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutPlanExerciseRepository extends JpaRepository<WorkoutPlanExercise, Long> {

    List<WorkoutPlanExercise> findByWorkoutPlanIdOrderByOrderIndex(Long planId);

    void deleteByWorkoutPlanId(Long planId);

    Optional<WorkoutPlanExercise> findByWorkoutPlanIdAndId(Long planId, Long id);
}
