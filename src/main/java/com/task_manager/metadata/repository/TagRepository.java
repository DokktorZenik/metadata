package com.task_manager.metadata.repository;

import com.task_manager.metadata.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByOrganizationId(Long id);
    Optional<Tag> findByIdAndOrganizationId(Long id, Long organizationId);
}
