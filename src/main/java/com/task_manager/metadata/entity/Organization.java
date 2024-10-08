package com.task_manager.metadata.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private Long ownerId;

}
