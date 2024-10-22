package com.task_manager.metadata.project;

import com.github.slugify.Slugify;
import com.task_manager.metadata.organization.OrgService;
import com.task_manager.metadata.exception.ResourceNotFoundException;
import com.task_manager.metadata.project.models.ProjectCreateRequest;
import com.task_manager.metadata.project.models.ProjectEntity;
import com.task_manager.metadata.project.models.ProjectUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final OrgService orgService;

    private final static Slugify slugify = new Slugify().withUnderscoreSeparator(true).withCustomReplacement("-", "_");


    private ProjectEntity getProjectByNameAndOrganizationId(String projectName, Long id) {
        return projectRepository.findByNameAndOrganizationId(projectName, id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "name", projectName));
    }


    public List<ProjectEntity> getAllProjectsByOrganizationName(String orgName) {
        //TODO check if user in organization

        return projectRepository.findAllByOrgId(orgService.getOrgIdByName(orgName));

    }

    public ProjectEntity createProject(String orgName, ProjectCreateRequest projectCreateRequest) {

        Long orgId = orgService.getOrgIdByName(orgName);

        projectRepository.findByTitleAndOrganizationId(projectCreateRequest.getTitle(), orgId)
                .ifPresent(existingProjectEntity -> {
                    throw new IllegalArgumentException("Project with title '" + projectCreateRequest.getTitle() + "' already exists in this organization.");
                });


        projectCreateRequest.setOwnerId(projectCreateRequest.getOwnerId());

        String projectName = slugify.slugify(projectCreateRequest.getTitle());

        return projectRepository.save(projectCreateRequest.toEntity(orgId, projectName));

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
    }

}
