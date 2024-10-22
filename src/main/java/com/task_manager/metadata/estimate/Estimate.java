package com.task_manager.metadata.estimate;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Estimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long organizationId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String color;

}
