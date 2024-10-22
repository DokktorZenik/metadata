package com.task_manager.metadata.repository;

import com.task_manager.metadata.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status,Long> {
    Optional<Status> findByIdAndOrganizationId(Long id, Long organizationId);
    List<Status> findByOrganizationId(Long id);
}
