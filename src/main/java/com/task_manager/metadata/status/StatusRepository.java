package com.task_manager.metadata.status;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status,Long> {
    Optional<Status> findByIdAndOrgId(Long id, Long organizationId);
    List<Status> findByOrgId(Long id);
}
