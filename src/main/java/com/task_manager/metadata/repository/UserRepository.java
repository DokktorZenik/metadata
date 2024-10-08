package com.task_manager.metadata.repository;

import com.task_manager.metadata.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
