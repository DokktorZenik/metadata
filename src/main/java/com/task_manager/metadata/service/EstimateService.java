package com.task_manager.metadata.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_manager.metadata.entity.Estimate;
import com.task_manager.metadata.entity.Organization;
import com.task_manager.metadata.exception.ResourceNotFoundException;
import com.task_manager.metadata.repository.EstimateRepository;
import com.task_manager.metadata.repository.OrganizationRepository;
import com.task_manager.metadata.response.EstimateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstimateService {
    private final EstimateRepository estimateRepository;
    private final OrganizationRepository organizationRepository;
    private final ObjectMapper objectMapper;

    private EstimateResponse mapToEstimateResponse(Estimate estimate){
        JsonNode content = objectMapper.createObjectNode()
                .put("title", estimate.getTitle())
                .put("color", estimate.getColor());

        return new EstimateResponse(estimate.getId(), estimate.getOrganizationId(), content);
    }

    private Organization getOrganizationByName(String organizationName) {
        return organizationRepository.findByName(organizationName)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "name", organizationName));
    }


    public EstimateResponse createEstimate(String organizationName, Estimate estimate){
        Organization organization = getOrganizationByName(organizationName);

        estimate.setOrganizationId(organization.getId());

        return mapToEstimateResponse(estimateRepository.save(estimate));
    }

    public List<EstimateResponse> getAllEstimates(String organizationName){
        Organization organization = getOrganizationByName(organizationName);


        return estimateRepository.findByOrganizationId(organization.getId())
                .stream().map(estimate -> mapToEstimateResponse(estimate)).toList();
    }

    public EstimateResponse getEstimateById(String organizationName, Long id){
        Organization organization = getOrganizationByName(organizationName);

        return mapToEstimateResponse(estimateRepository.findByIdAndOrganizationId(id, organization.getId())
                .orElseThrow(()->new ResourceNotFoundException("Estimate", "id", id)));
    }

    public EstimateResponse updateEstimate(String organizationName, Long id, Estimate estimate){
        Organization organization = getOrganizationByName(organizationName);


        Estimate updatedEstimate = estimateRepository.findByIdAndOrganizationId(id,organization.getId())
                .orElseThrow(()->new ResourceNotFoundException("Estimate", "id", id));

        updatedEstimate.setTitle(estimate.getTitle());
        updatedEstimate.setColor(estimate.getColor());

        return mapToEstimateResponse(estimateRepository.save(updatedEstimate));
    }

    public void deleteEstimate(String organizationName, Long id){
        Organization organization = getOrganizationByName(organizationName);

        Estimate estimate = estimateRepository.findByIdAndOrganizationId(id, organization.getId())
                .orElseThrow(()->new ResourceNotFoundException("Estimate", "id", id));

        estimateRepository.delete(estimate);
    }
}
