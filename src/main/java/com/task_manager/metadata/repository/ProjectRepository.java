package com.task_manager.metadata.repository;

import com.task_manager.metadata.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByNameAndOrganizationId(String name, Long id);
    @Query(value = "SELECT p from Project p JOIN Organization o ON p.organizationId = o.id where o.name = :name")
    List<Project> findByOrganizationName(@Param("name") String name);
}
