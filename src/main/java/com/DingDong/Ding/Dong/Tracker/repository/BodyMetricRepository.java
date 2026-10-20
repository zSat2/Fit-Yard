package com.DingDong.Ding.Dong.Tracker.repository;

import com.DingDong.Ding.Dong.Tracker.entity.UserBodyMetric;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BodyMetricRepository extends JpaRepository<UserBodyMetric, Long> {

    List<UserBodyMetric> findByUserIdOrderByRecordedAtDesc(Long userId);

    Page<UserBodyMetric> findByUserIdOrderByRecordedAtDesc(Long userId, Pageable pageable);

    Optional<UserBodyMetric> findFirstByUserIdOrderByRecordedAtDesc(Long userId);
}
