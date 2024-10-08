package com.task_manager.metadata.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_manager.metadata.entity.Estimate;
import com.task_manager.metadata.entity.Organization;
import com.task_manager.metadata.entity.Project;
import com.task_manager.metadata.exception.ResourceNotFoundException;
import com.task_manager.metadata.repository.EstimateRepository;
import com.task_manager.metadata.repository.OrganizationRepository;
import com.task_manager.metadata.repository.ProjectRepository;
import com.task_manager.metadata.response.EstimateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstimateService {
    private final EstimateRepository estimateRepository;
    private final OrganizationRepository organizationRepository;
    private final ProjectRepository projectRepository;
    private final ObjectMapper objectMapper;

    private EstimateResponse mapToEstimateResponse(Estimate estimate){
        JsonNode content = objectMapper.createObjectNode()
                .put("title", estimate.getTitle())
                .put("color", estimate.getColor());

        return new EstimateResponse(estimate.getId(), estimate.getOrganizationId(), estimate.getProjectId(), content);
    }

    private Organization getOrganizationByName(String organizationName) {
        return organizationRepository.findByName(organizationName)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "name", organizationName));
    }

    private Project getProjectByNameAndOrganizationId(String projectName,Long id) {
        return projectRepository.findByNameAndOrganizationId(projectName,id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "name", projectName));
    }


    public EstimateResponse createEstimate(String organizationName, String projectName, Estimate estimate){
        Organization organization = getOrganizationByName(organizationName);
        Project project = getProjectByNameAndOrganizationId(projectName, organization.getId());

        estimate.setOrganizationId(organization.getId());
        estimate.setProjectId(project.getId());

        return mapToEstimateResponse(estimateRepository.save(estimate));
    }

    public List<EstimateResponse> getAllEstimates(String organizationName, String projectName){
        Organization organization = getOrganizationByName(organizationName);

        Project project = getProjectByNameAndOrganizationId(projectName, organization.getId());

        return estimateRepository.findByProjectId(project.getId())
                .stream().map(estimate -> mapToEstimateResponse(estimate)).toList();
    }

    public EstimateResponse getEstimateById(String organizationName, String projectName, Long id){
        Organization organization = getOrganizationByName(organizationName);

        Project project = getProjectByNameAndOrganizationId(projectName, organization.getId());

        return mapToEstimateResponse(estimateRepository.findByIdAndProjectId(id, project.getId())
                .orElseThrow(()->new ResourceNotFoundException("Estimate", "id", id)));
    }

    public EstimateResponse updateEstimate(String organizationName, String projectName, Long id, Estimate estimate){
        Organization organization = getOrganizationByName(organizationName);

        Project project = getProjectByNameAndOrganizationId(projectName, organization.getId());

        Estimate updatedEstimate = estimateRepository.findByIdAndProjectId(id,project.getId())
                .orElseThrow(()->new ResourceNotFoundException("Estimate", "id", id));

        updatedEstimate.setTitle(estimate.getTitle());
        updatedEstimate.setColor(estimate.getColor());

        return mapToEstimateResponse(estimateRepository.save(updatedEstimate));
    }

    public void deleteEstimate(String organizationName, String projectName, Long id){
        Organization organization = getOrganizationByName(organizationName);

        Project project = getProjectByNameAndOrganizationId(projectName, organization.getId());

        Estimate estimate = estimateRepository.findByIdAndProjectId(id, project.getId())
                .orElseThrow(()->new ResourceNotFoundException("Estimate", "id", id));

        estimateRepository.delete(estimate);
    }
}
