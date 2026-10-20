package com.DingDong.Ding.Dong.Tracker.repository;

import com.DingDong.Ding.Dong.Tracker.entity.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    Page<Exercise> findByIsActiveTrueAndDeletedAtIsNull(Pageable pageable);

    Page<Exercise> findByCategoryAndIsActiveTrueAndDeletedAtIsNull(String category, Pageable pageable);

    Page<Exercise> findByPrimaryMuscleGroupAndIsActiveTrueAndDeletedAtIsNull(String primaryMuscleGroup, Pageable pageable);

    Optional<Exercise> findBySlugAndDeletedAtIsNull(String slug);

    boolean existsByNameAndDeletedAtIsNull(String name);

    @Query("SELECT e FROM Exercise e WHERE e.isActive = true AND e.deletedAt IS NULL AND " +
           "(LOWER(e.name) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(e.description) LIKE LOWER(CONCAT('%', :q, '%')))")
    Page<Exercise> search(@Param("q") String query, Pageable pageable);

    List<Exercise> findByCreatedByUserIdAndDeletedAtIsNull(Long userId);

    Optional<Exercise> findByIdAndDeletedAtIsNull(Long id);

    boolean existsBySlugAndDeletedAtIsNull(String slug);
}
