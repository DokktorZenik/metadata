package com.task_manager.metadata.estimate;

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
public class EstimateService {
    private final EstimateRepository estimateRepository;
    private final OrgRepository orgRepository;
    private final ObjectMapper objectMapper;

    private EstimateResponse mapToEstimateResponse(Estimate estimate){
        JsonNode content = objectMapper.createObjectNode()
                .put("title", estimate.getTitle())
                .put("color", estimate.getColor());

        return new EstimateResponse(estimate.getId(), estimate.getOrganizationId(), content);
    }

    private OrgEntity getOrganizationByName(String organizationName) {
        return orgRepository.findByName(organizationName)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "name", organizationName));
    }


    public EstimateResponse createEstimate(String organizationName, Estimate estimate){
        OrgEntity orgEntity = getOrganizationByName(organizationName);

        estimate.setOrganizationId(orgEntity.getId());

        return mapToEstimateResponse(estimateRepository.save(estimate));
    }

    public List<EstimateResponse> getAllEstimates(String organizationName){
        OrgEntity orgEntity = getOrganizationByName(organizationName);


        return estimateRepository.findByOrganizationId(orgEntity.getId())
                .stream().map(estimate -> mapToEstimateResponse(estimate)).toList();
    }

    public EstimateResponse getEstimateById(String organizationName, Long id){
        OrgEntity orgEntity = getOrganizationByName(organizationName);

        return mapToEstimateResponse(estimateRepository.findByIdAndOrganizationId(id, orgEntity.getId())
                .orElseThrow(()->new ResourceNotFoundException("Estimate", "id", id)));
    }

    public EstimateResponse updateEstimate(String organizationName, Long id, Estimate estimate){
        OrgEntity orgEntity = getOrganizationByName(organizationName);


        Estimate updatedEstimate = estimateRepository.findByIdAndOrganizationId(id, orgEntity.getId())
                .orElseThrow(()->new ResourceNotFoundException("Estimate", "id", id));

        updatedEstimate.setTitle(estimate.getTitle());
        updatedEstimate.setColor(estimate.getColor());

        return mapToEstimateResponse(estimateRepository.save(updatedEstimate));
    }

    public void deleteEstimate(String organizationName, Long id){
        OrgEntity orgEntity = getOrganizationByName(organizationName);

        Estimate estimate = estimateRepository.findByIdAndOrganizationId(id, orgEntity.getId())
                .orElseThrow(()->new ResourceNotFoundException("Estimate", "id", id));

        estimateRepository.delete(estimate);
    }
}
