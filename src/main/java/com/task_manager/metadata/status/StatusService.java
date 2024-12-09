package com.task_manager.metadata.status;

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
public class StatusService {
    private final StatusRepository statusRepository;
    private final OrgRepository orgRepository;
    private final ObjectMapper objectMapper;

    private StatusResponse mapToStatusResponse(Status status){
        JsonNode content = objectMapper.createObjectNode()
                .put("title", status.getTitle())
                .put("color", status.getColor());

        return new StatusResponse(status.getId(), status.getOrgId(), content);
    }

    private OrgEntity getOrganizationByName(String organizationName) {
        return orgRepository.findByName(organizationName)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "name", organizationName));
    }

    public StatusResponse createStatus(String organizationName, Status status){
        OrgEntity orgEntity = getOrganizationByName(organizationName);

        status.setOrgId(orgEntity.getId());

        return mapToStatusResponse(statusRepository.save(status));
    }

    public List<StatusResponse> getAllStatuses(String organizationName){
        OrgEntity orgEntity = getOrganizationByName(organizationName);

        return statusRepository.findByOrgId(orgEntity.getId())
                .stream().map(priority -> mapToStatusResponse(priority)).toList();
    }

    public StatusResponse getStatusById(String organizationName, Long id){
        OrgEntity orgEntity = getOrganizationByName(organizationName);

        return mapToStatusResponse(statusRepository.findByIdAndOrgId(id, orgEntity.getId())
                .orElseThrow(()->new ResourceNotFoundException("Status", "id", id)));
    }

    public StatusResponse updateStatus(String organizationName, Long id, Status status){
        OrgEntity orgEntity = getOrganizationByName(organizationName);

        Status updatedStatus = statusRepository.findByIdAndOrgId(id, orgEntity.getId())
                .orElseThrow(()->new ResourceNotFoundException("Status", "id", id));

        updatedStatus.setTitle(status.getTitle());
        updatedStatus.setColor(status.getColor());

        return mapToStatusResponse(statusRepository.save(updatedStatus));
    }

    public void deleteStatus(String organizationName, Long id){
        OrgEntity orgEntity = getOrganizationByName(organizationName);


        Status status = statusRepository.findByIdAndOrgId(id, orgEntity.getId())
                .orElseThrow(()->new ResourceNotFoundException("Status", "id", id));

        statusRepository.delete(status);
    }
}
