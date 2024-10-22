package com.task_manager.metadata.project;

import com.task_manager.metadata.project.models.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    Optional<ProjectEntity> findByNameAndOrganizationId(String name, Long id);

    List<ProjectEntity> findAllByOrgId(Long orgId);

    Optional<ProjectEntity> findByTitleAndOrganizationId(String title, Long id);
}
