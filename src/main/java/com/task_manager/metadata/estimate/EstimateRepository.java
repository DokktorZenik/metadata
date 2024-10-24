package com.task_manager.metadata.estimate;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {
    Optional<Estimate> findByIdAndOrganizationId(Long id, Long projectId);
    List<Estimate> findByOrganizationId(Long id);

}
