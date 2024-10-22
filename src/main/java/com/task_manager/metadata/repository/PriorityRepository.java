package com.task_manager.metadata.repository;

import com.task_manager.metadata.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PriorityRepository extends JpaRepository<Priority,Long> {
    Optional<Priority> findByIdAndOrganizationId(Long id, Long projectId);
    List<Priority> findByOrganizationId(Long id);
}
