package com.task_manager.metadata.request;

import com.task_manager.metadata.entity.Organization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrganizationRequest {
    private String title;
    private String name;
    private Long ownerId;

    public Organization toEntity(){
        return Organization.builder()
                .title(title)
                .name(name)
                .ownerId(ownerId)
                .build();
    }
}
