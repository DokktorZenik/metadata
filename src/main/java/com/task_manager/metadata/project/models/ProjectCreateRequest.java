package com.task_manager.metadata.project.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCreateRequest {

    private String title;

    private Long ownerId;

    public ProjectEntity toEntity(Long orgId, String name){
        return ProjectEntity.builder()
                .title(title)
                .name(name)
                .ownerId(ownerId)
                .organizationId(orgId)
                .build();
    }
}
