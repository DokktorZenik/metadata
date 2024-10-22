package com.task_manager.metadata.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_manager.metadata.entity.Organization;
import com.task_manager.metadata.entity.Status;
import com.task_manager.metadata.exception.ResourceNotFoundException;
import com.task_manager.metadata.repository.OrganizationRepository;
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
    private final ObjectMapper objectMapper;

    private StatusResponse mapToStatusResponse(Status status){
        JsonNode content = objectMapper.createObjectNode()
                .put("title", status.getTitle())
                .put("color", status.getColor());

        return new StatusResponse(status.getId(), status.getOrganizationId(), content);
    }

    private Organization getOrganizationByName(String organizationName) {
        return organizationRepository.findByName(organizationName)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "name", organizationName));
    }

    public StatusResponse createStatus(String organizationName, Status status){
        Organization organization = getOrganizationByName(organizationName);

        status.setOrganizationId(organization.getId());

        return mapToStatusResponse(statusRepository.save(status));
    }

    public List<StatusResponse> getAllStatuses(String organizationName){
        Organization organization = getOrganizationByName(organizationName);

        return statusRepository.findByOrganizationId(organization.getId())
                .stream().map(priority -> mapToStatusResponse(priority)).toList();
    }

    public StatusResponse getStatusById(String organizationName, Long id){
        Organization organization = getOrganizationByName(organizationName);

        return mapToStatusResponse(statusRepository.findByIdAndOrganizationId(id,organization.getId())
                .orElseThrow(()->new ResourceNotFoundException("Status", "id", id)));
    }

    public StatusResponse updateStatus(String organizationName, Long id, Status status){
        Organization organization = getOrganizationByName(organizationName);

        Status updatedStatus = statusRepository.findByIdAndOrganizationId(id, organization.getId())
                .orElseThrow(()->new ResourceNotFoundException("Status", "id", id));

        updatedStatus.setTitle(status.getTitle());
        updatedStatus.setColor(status.getColor());

        return mapToStatusResponse(statusRepository.save(updatedStatus));
    }

    public void deleteStatus(String organizationName, Long id){
        Organization organization = getOrganizationByName(organizationName);


        Status status = statusRepository.findByIdAndOrganizationId(id, organization.getId())
                .orElseThrow(()->new ResourceNotFoundException("Status", "id", id));

        statusRepository.delete(status);
    }
}
