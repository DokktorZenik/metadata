package com.task_manager.metadata.priority;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_manager.metadata.organization.models.OrgEntity;
import com.task_manager.metadata.exception.ResourceNotFoundException;
import com.task_manager.metadata.organization.OrgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PriorityService {
    private final PriorityRepository priorityRepository;
    private final OrgRepository orgRepository;
    private final ObjectMapper objectMapper;

    private PriorityResponse mapToPriorityResponse(Priority priority){
        JsonNode content = objectMapper.createObjectNode()
                .put("title", priority.getTitle())
                .put("color", priority.getColor());

        return new PriorityResponse(priority.getId(), priority.getOrganizationId(), content);
    }

    private OrgEntity getOrganizationByName(String organizationName) {
        return orgRepository.findByName(organizationName)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "name", organizationName));
    }

    public PriorityResponse createPriority(String organizationName, Priority priority){
        OrgEntity orgEntity = getOrganizationByName(organizationName);

        priority.setOrganizationId(orgEntity.getId());

        return mapToPriorityResponse(priorityRepository.save(priority));
    }

    public List<PriorityResponse> getAllPriorities(String organizationName){
        OrgEntity orgEntity = getOrganizationByName(organizationName);

        return priorityRepository.findByOrganizationId(orgEntity.getId())
                .stream().map(priority -> mapToPriorityResponse(priority)).toList();
    }

    public PriorityResponse getPriorityById(String organizationName, Long id){
        OrgEntity orgEntity = getOrganizationByName(organizationName);

        return mapToPriorityResponse(priorityRepository.findByIdAndOrganizationId(id, orgEntity.getId())
                .orElseThrow(()->new ResourceNotFoundException("Priority", "id", id)));
    }

    public PriorityResponse updatePriority(String organizationName, Long id, Priority priority){
        OrgEntity orgEntity = getOrganizationByName(organizationName);

        Priority updatedPriority = priorityRepository.findByIdAndOrganizationId(id, orgEntity.getId())
                .orElseThrow(()->new ResourceNotFoundException("Priority", "id", id));

        updatedPriority.setTitle(priority.getTitle());
        updatedPriority.setColor(priority.getColor());

        return mapToPriorityResponse(priorityRepository.save(updatedPriority));
    }

    public void deletePriority(String organizationName, Long id){
        OrgEntity orgEntity = getOrganizationByName(organizationName);

        Priority priority = priorityRepository.findByIdAndOrganizationId(id, orgEntity.getId())
                .orElseThrow(()->new ResourceNotFoundException("Priority", "id", id));

        priorityRepository.delete(priority);
    }
}
