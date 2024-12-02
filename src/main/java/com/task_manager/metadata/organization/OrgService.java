package com.task_manager.metadata.organization;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.slugify.Slugify;
import com.task_manager.metadata.exception.ResourceNotFoundException;
import com.task_manager.metadata.organization.models.OrgCreateRequest;
import com.task_manager.metadata.organization.models.OrgEntity;
import com.task_manager.metadata.organization.models.OrgUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrgService {
    private final RestClient restClient;
    private ObjectNode organizationJsonObject;
    private final String userServiceUrl = "http://localhost:8083";
    private final ObjectMapper objectMapper;

    private final OrgRepository orgRepository;

    private final static Slugify slugify = new Slugify().withUnderscoreSeparator(true).withCustomReplacement("-", "_");

    public void sendRequest(Long organizationId, Long ownerId, String endpoint, String method){

        organizationJsonObject = objectMapper.createObjectNode();
        organizationJsonObject.put("organizationId", organizationId);
        organizationJsonObject.put("ownerId", ownerId);

        if(method.equals("POST")) {
            ObjectNode retrieve = restClient.post().uri(userServiceUrl + endpoint).body(organizationJsonObject).retrieve().toEntity(ObjectNode.class).getBody();
        }
        else{
            ObjectNode retrieve = restClient.delete().uri(userServiceUrl + endpoint + "/organization/" + organizationId + "/owner/" + ownerId).retrieve().toEntity(ObjectNode.class).getBody();
        }
    }


    public OrgEntity getOrganizationByName(String organizationName) {
        return orgRepository.findByName(organizationName)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "name", organizationName));
    }

    public Long getOrgIdByName(String organizationName) {
        Optional<Long> idByName = orgRepository.getIdByName(organizationName);

        if(idByName.isPresent()) {
            return idByName.get();
        } else {
            throw new ResourceNotFoundException("Organization", "name", organizationName);
        }
    }

    public List<OrgEntity> getAllOrganizations(){
       return orgRepository.findAll();
    }

    public OrgEntity createOrganization(OrgCreateRequest request){

        if(orgRepository.findByName(request.getTitle()).isPresent())
            throw new IllegalArgumentException("Organization with name %s already exists".formatted(request.getTitle()));

        String name = slugify.slugify(request.getTitle());

        OrgEntity savedOrganization = orgRepository.save(
                request.toEntity(name));

        sendRequest(savedOrganization.getId(), savedOrganization.getOwnerId(), "/v1/organization/create", "POST");

        return savedOrganization;
    }

    public OrgEntity updateOrganization(String oldName, OrgUpdateRequest updatedOrganization){

        String title = updatedOrganization.getTitle();

        String slugifyTitle = slugify.slugify(title);

        return orgRepository.updateOrg(oldName, title, slugifyTitle);
    }

    @Transactional
    public void deleteOrganization(String name){
        OrgEntity organization = orgRepository.findByName(name).orElseThrow();

        orgRepository.deleteByName(name);

        sendRequest(organization.getId(), organization.getOwnerId(), "/v1/organization/delete","DELETE");
    }

}
