package com.task_manager.metadata.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_manager.metadata.entity.Organization;
import com.task_manager.metadata.entity.Project;
import com.task_manager.metadata.entity.Status;
import com.task_manager.metadata.exception.ResourceNotFoundException;
import com.task_manager.metadata.repository.OrganizationRepository;
import com.task_manager.metadata.repository.ProjectRepository;
import com.task_manager.metadata.repository.StatusRepository;
import com.task_manager.metadata.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatusService {
    private final StatusRepository statusRepository;
    private final OrganizationRepository organizationRepository;
    private final ProjectRepository projectRepository;
    private final ObjectMapper objectMapper;

    private StatusResponse mapToStatusResponse(Status status){
        JsonNode content = objectMapper.createObjectNode()
                .put("title", status.getTitle())
                .put("color", status.getColor());

        return new StatusResponse(status.getId(), status.getOrganizationId(), status.getProjectId(), content);
    }

    private Organization getOrganizationByName(String organizationName) {
        return organizationRepository.findByName(organizationName)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "name", organizationName));
    }

    private Project getProjectByNameAndOrganizationId(String projectName,Long id) {
        return projectRepository.findByNameAndOrganizationId(projectName,id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "name", projectName));
    }


    public StatusResponse createStatus(String organizationName, String projectName, Status status){
        Organization organization = getOrganizationByName(organizationName);

        Project project = getProjectByNameAndOrganizationId(projectName, organization.getId());

        status.setOrganizationId(organization.getId());
        status.setProjectId(project.getId());

        return mapToStatusResponse(statusRepository.save(status));
    }

    public List<StatusResponse> getAllStatuses(String organizationName, String projectName){
        Organization organization = getOrganizationByName(organizationName);

        Project project = getProjectByNameAndOrganizationId(projectName, organization.getId());

        return statusRepository.findByProjectId(project.getId())
                .stream().map(priority -> mapToStatusResponse(priority)).toList();
    }

    public StatusResponse getStatusById(String organizationName, String projectName, Long id){
        Organization organization = getOrganizationByName(organizationName);

        Project project = getProjectByNameAndOrganizationId(projectName, organization.getId());

        return mapToStatusResponse(statusRepository.findByIdAndProjectId(id,project.getId())
                .orElseThrow(()->new ResourceNotFoundException("Status", "id", id)));
    }

    public StatusResponse updateStatus(String organizationName, String projectName, Long id, Status status){
        Organization organization = getOrganizationByName(organizationName);

        Project project = getProjectByNameAndOrganizationId(projectName, organization.getId());

        Status updatedStatus = statusRepository.findByIdAndProjectId(id, project.getId())
                .orElseThrow(()->new ResourceNotFoundException("Status", "id", id));

        updatedStatus.setTitle(status.getTitle());
        updatedStatus.setColor(status.getColor());

        return mapToStatusResponse(statusRepository.save(updatedStatus));
    }

    public void deleteStatus(String organizationName, String projectName, Long id){
        Organization organization = getOrganizationByName(organizationName);

        Project project = getProjectByNameAndOrganizationId(projectName, organization.getId());

        Status status = statusRepository.findByIdAndProjectId(id, project.getId())
                .orElseThrow(()->new ResourceNotFoundException("Status", "id", id));

        statusRepository.delete(status);
    }
}
