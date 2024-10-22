package com.task_manager.metadata.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_manager.metadata.entity.Organization;
import com.task_manager.metadata.entity.Priority;
import com.task_manager.metadata.exception.ResourceNotFoundException;
import com.task_manager.metadata.repository.OrganizationRepository;
import com.task_manager.metadata.repository.PriorityRepository;
import com.task_manager.metadata.response.PriorityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PriorityService {
    private final PriorityRepository priorityRepository;
    private final OrganizationRepository organizationRepository;
    private final ObjectMapper objectMapper;

    private PriorityResponse mapToPriorityResponse(Priority priority){
        JsonNode content = objectMapper.createObjectNode()
                .put("title", priority.getTitle())
                .put("color", priority.getColor());

        return new PriorityResponse(priority.getId(), priority.getOrganizationId(), content);
    }

    private Organization getOrganizationByName(String organizationName) {
        return organizationRepository.findByName(organizationName)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "name", organizationName));
    }

    public PriorityResponse createPriority(String organizationName, Priority priority){
        Organization organization = getOrganizationByName(organizationName);

        priority.setOrganizationId(organization.getId());

        return mapToPriorityResponse(priorityRepository.save(priority));
    }

    public List<PriorityResponse> getAllPriorities(String organizationName){
        Organization organization = getOrganizationByName(organizationName);

        return priorityRepository.findByOrganizationId(organization.getId())
                .stream().map(priority -> mapToPriorityResponse(priority)).toList();
    }

    public PriorityResponse getPriorityById(String organizationName, Long id){
        Organization organization = getOrganizationByName(organizationName);

        return mapToPriorityResponse(priorityRepository.findByIdAndOrganizationId(id, organization.getId())
                .orElseThrow(()->new ResourceNotFoundException("Priority", "id", id)));
    }

    public PriorityResponse updatePriority(String organizationName, Long id, Priority priority){
        Organization organization = getOrganizationByName(organizationName);

        Priority updatedPriority = priorityRepository.findByIdAndOrganizationId(id,organization.getId())
                .orElseThrow(()->new ResourceNotFoundException("Priority", "id", id));

        updatedPriority.setTitle(priority.getTitle());
        updatedPriority.setColor(priority.getColor());

        return mapToPriorityResponse(priorityRepository.save(updatedPriority));
    }

    public void deletePriority(String organizationName, Long id){
        Organization organization = getOrganizationByName(organizationName);

        Priority priority = priorityRepository.findByIdAndOrganizationId(id,organization.getId())
                .orElseThrow(()->new ResourceNotFoundException("Priority", "id", id));

        priorityRepository.delete(priority);
    }
}
