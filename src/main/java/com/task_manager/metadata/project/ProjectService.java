package com.task_manager.metadata.project;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.slugify.Slugify;
import com.task_manager.metadata.organization.OrgService;
import com.task_manager.metadata.exception.ResourceNotFoundException;
import com.task_manager.metadata.project.models.ProjectCreateRequest;
import com.task_manager.metadata.project.models.ProjectEntity;
import com.task_manager.metadata.project.models.ProjectUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ObjectMapper objectMapper;

    private final ProjectRepository projectRepository;

    private final OrgService orgService;

    private final static Slugify slugify = new Slugify().withUnderscoreSeparator(true).withCustomReplacement("-", "_");

    private ObjectNode projectJsonObject;

    private final RestClient restClient;

    @Value("${user.service.url}")
    private String userServiceUrl;

    public void sendRequest(Long projectId, Long ownerId, String endpoint, String method){

        projectJsonObject = objectMapper.createObjectNode();

        projectJsonObject.put("projectId", projectId);
        projectJsonObject.put("ownerId", ownerId);

        if(method.equals("POST")) {
            ObjectNode retrieve = restClient.post().uri(userServiceUrl + endpoint).body(projectJsonObject).retrieve().toEntity(ObjectNode.class).getBody();
        }
        else{
            ObjectNode retrieve = restClient.delete().uri(userServiceUrl + endpoint + "/project/" + projectId + "/owner/" + ownerId).retrieve().toEntity(ObjectNode.class).getBody();
        }
    }


    private ProjectEntity getProjectByNameAndOrganizationId(String projectName, Long id) {
        return projectRepository.findByNameAndOrgId(projectName, id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "name", projectName));
    }


    public List<ProjectEntity> getAllProjectsByOrganizationName(String orgName) {
        //TODO check if user in organization

        return projectRepository.findAllByOrgId(orgService.getOrgIdByName(orgName));

    }

    public ProjectEntity createProject(String orgName, ProjectCreateRequest projectCreateRequest) {

        Long orgId = orgService.getOrgIdByName(orgName);

        projectRepository.findByTitleAndOrgId(projectCreateRequest.getTitle(), orgId)
                .ifPresent(existingProjectEntity -> {
                    throw new IllegalArgumentException("Project with title '" + projectCreateRequest.getTitle() + "' already exists in this organization.");
                });


        projectCreateRequest.setOwnerId(projectCreateRequest.getOwnerId());

        String projectName = slugify.slugify(projectCreateRequest.getTitle());

        ProjectEntity createdProject = projectRepository.save(projectCreateRequest.toEntity(orgId, projectName));

//        sendRequest(createdProject.getId(), createdProject.getOwnerId(), "/v1/project/create", "POST");

        return createdProject;
    }

    public ProjectEntity getProjectByName(String orgName, String projectName) {

        Long orgId = orgService.getOrgIdByName(orgName);
        return getProjectByNameAndOrganizationId(projectName, orgId);

    }

    public ProjectEntity updateProject(String orgName, String projectName, ProjectUpdateRequest newProject) {

        Long orgId = orgService.getOrgIdByName(orgName);
        ProjectEntity projectEntity = getProjectByNameAndOrganizationId(projectName, orgId);

        projectEntity.setTitle(newProject.getTitle());
        projectEntity.setName(slugify.slugify(newProject.getTitle()));

        return projectRepository.save(projectEntity);
    }

    public void deleteProject(String orgName, String projectName) {

        Long orgId = orgService.getOrgIdByName(orgName);
        ProjectEntity projectEntity = getProjectByNameAndOrganizationId(projectName, orgId);

        projectRepository.delete(projectEntity);

//        sendRequest(projectEntity.getId(), projectEntity.getOwnerId(), "/v1/project/delete","DELETE");
    }

}
