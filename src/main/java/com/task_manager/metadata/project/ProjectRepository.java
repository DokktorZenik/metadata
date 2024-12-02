package com.task_manager.metadata.project;

import com.task_manager.metadata.project.models.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    Optional<ProjectEntity> findByNameAndOrgId(String name, Long id);

    List<ProjectEntity> findAllByOrgId(Long orgId);

    Optional<ProjectEntity> findByTitleAndOrgId(String title, Long id);
}
