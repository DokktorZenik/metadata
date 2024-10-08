package com.task_manager.metadata.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_manager.metadata.entity.Organization;
import com.task_manager.metadata.entity.Priority;
import com.task_manager.metadata.entity.Project;
import com.task_manager.metadata.exception.ResourceNotFoundException;
import com.task_manager.metadata.repository.OrganizationRepository;
import com.task_manager.metadata.repository.PriorityRepository;
import com.task_manager.metadata.repository.ProjectRepository;
import com.task_manager.metadata.response.PriorityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PriorityService {
    private final PriorityRepository priorityRepository;
    private final OrganizationRepository organizationRepository;
    private final ProjectRepository projectRepository;
    private final ObjectMapper objectMapper;

    private PriorityResponse mapToPriorityResponse(Priority priority){
        JsonNode content = objectMapper.createObjectNode()
                .put("title", priority.getTitle())
                .put("color", priority.getColor());

        return new PriorityResponse(priority.getId(), priority.getOrganizationId(), priority.getProjectId(), content);
    }

    private Organization getOrganizationByName(String organizationName) {
        return organizationRepository.findByName(organizationName)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "name", organizationName));
    }

    private Project getProjectByNameAndOrganizationId(String projectName,Long id) {
        return projectRepository.findByNameAndOrganizationId(projectName,id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "name", projectName));
    }

    public PriorityResponse createPriority(String organizationName, String projectName, Priority priority){
        Organization organization = getOrganizationByName(organizationName);
        Project project = getProjectByNameAndOrganizationId(projectName, organization.getId());

        priority.setOrganizationId(organization.getId());
        priority.setProjectId(project.getId());

        return mapToPriorityResponse(priorityRepository.save(priority));
    }

    public List<PriorityResponse> getAllPriorities(String organizationName, String projectName){
        Organization organization = getOrganizationByName(organizationName);

        Project project = getProjectByNameAndOrganizationId(projectName, organization.getId());

        return priorityRepository.findByProjectId(project.getId())
                .stream().map(priority -> mapToPriorityResponse(priority)).toList();
    }

    public PriorityResponse getPriorityById(String organizationName, String projectName, Long id){
        Organization organization = getOrganizationByName(organizationName);

        Project project = getProjectByNameAndOrganizationId(projectName, organization.getId());

        return mapToPriorityResponse(priorityRepository.findByIdAndProjectId(id,project.getId())
                .orElseThrow(()->new ResourceNotFoundException("Priority", "id", id)));
    }

    public PriorityResponse updatePriority(String organizationName, String projectName, Long id, Priority priority){
        Organization organization = getOrganizationByName(organizationName);

        Project project = getProjectByNameAndOrganizationId(projectName, organization.getId());

        Priority updatedPriority = priorityRepository.findByIdAndProjectId(id,project.getId())
                .orElseThrow(()->new ResourceNotFoundException("Priority", "id", id));

        updatedPriority.setTitle(priority.getTitle());
        updatedPriority.setColor(priority.getColor());

        return mapToPriorityResponse(priorityRepository.save(updatedPriority));
    }

    public void deletePriority(String organizationName, String projectName, Long id){
        Organization organization = getOrganizationByName(organizationName);

        Project project = getProjectByNameAndOrganizationId(projectName, organization.getId());

        Priority priority = priorityRepository.findByIdAndProjectId(id,project.getId())
                .orElseThrow(()->new ResourceNotFoundException("Priority", "id", id));

        priorityRepository.delete(priority);
    }
}
