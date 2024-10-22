package com.task_manager.metadata.project.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "project")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private Long orgId;

    public ProjectResponse toResponse() {
        return ProjectResponse
                .builder()
                .id(id)
                .title(title)
                .orgId(orgId)
                .build();
    }

}