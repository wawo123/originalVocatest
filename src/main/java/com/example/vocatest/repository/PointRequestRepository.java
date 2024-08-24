package com.example.vocatest.repository;

import com.example.vocatest.entity.PointRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PointRequestRepository extends JpaRepository<PointRequestEntity, Long> {
    List<PointRequestEntity> findByEmail(String email);

    Optional<PointRequestEntity> findById(Long id);
}
