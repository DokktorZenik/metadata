package com.task_manager.metadata.status;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Status {

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
