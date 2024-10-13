package com.task_manager.metadata.request;

import com.task_manager.metadata.entity.Organization;
import com.task_manager.metadata.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequest {
    private String title;
    private String name;
    private Long ownerId;
    private Long organizationId;

    public Project toEntity(){
        return Project.builder()
                .title(title)
                .name(name)
                .ownerId(ownerId)
                .organizationId(organizationId)
                .build();
    }
}
