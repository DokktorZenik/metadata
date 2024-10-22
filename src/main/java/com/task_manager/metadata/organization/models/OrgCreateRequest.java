package com.task_manager.metadata.organization.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrgCreateRequest {

    private String title;

    private Long ownerId;

    public OrgEntity toEntity(String name){
        return OrgEntity.builder()
                .title(title)
                .name(name)
                .ownerId(ownerId)
                .build();
    }
}
