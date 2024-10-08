package com.task_manager.metadata.repository;

import com.task_manager.metadata.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization,Long> {
    Optional<Organization> findByName(String name);
}
