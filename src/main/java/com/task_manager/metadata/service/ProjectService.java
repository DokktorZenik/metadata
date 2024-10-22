package com.task_manager.metadata.service;

import com.github.slugify.Slugify;
import com.task_manager.metadata.entity.Organization;
import com.task_manager.metadata.entity.Project;
import com.task_manager.metadata.exception.ResourceNotFoundException;
import com.task_manager.metadata.repository.OrganizationRepository;
import com.task_manager.metadata.repository.ProjectRepository;
import com.task_manager.metadata.request.CreateProjectRequest;
import com.task_manager.metadata.request.UpdateProjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final OrganizationRepository organizationRepository;
    private final static Slugify slugify = new Slugify().withUnderscoreSeparator(true).withCustomReplacement("-", "_");

    private Organization getOrganizationByName(String organizationName) {
        return organizationRepository.findByName(organizationName)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "name", organizationName));
    }

    private Project getProjectByNameAndOrganizationId(String projectName,Long id) {
        return projectRepository.findByNameAndOrganizationId(projectName,id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "name", projectName));
    }


    public List<Project> getAllProjectsByOrganizationName(String organizationName){
        //TODO check if user in organization
        if(organizationRepository.findByName(organizationName).isPresent())
            return projectRepository.findByOrganizationName(organizationName);
        else
            throw new ResourceNotFoundException("Organization","name", organizationName);
    }

    public Project createProject(String organizationName, CreateProjectRequest createProjectRequest){

        Organization organization = getOrganizationByName(organizationName);

        projectRepository.findByTitleAndOrganizationId(createProjectRequest.getTitle(), organization.getId())
                .ifPresent(existingProject -> {
                    throw new IllegalArgumentException("Project with title '" + createProjectRequest.getTitle() + "' already exists in this organization.");
                });


        createProjectRequest.setOwnerId(createProjectRequest.getOwnerId());
        createProjectRequest.setOrganizationId(organization.getId());
        createProjectRequest.setName(slugify.slugify(createProjectRequest.getTitle()));

        return projectRepository.save(createProjectRequest.toEntity());

    }

    public Project getProjectByName(String organizationName, String projectName){

        Organization organization = getOrganizationByName(organizationName);

        return getProjectByNameAndOrganizationId(projectName, organization.getId());
    }

    public Project updateProject(String organizationName, String projectName, UpdateProjectRequest newProject){

        Organization organization = getOrganizationByName(organizationName);
        Project project = getProjectByNameAndOrganizationId(projectName, organization.getId());

        project.setTitle(newProject.getTitle());
        project.setName(slugify.slugify(newProject.getTitle()));

        return projectRepository.save(project);
    }

    public void deleteProject(String organizationName, String projectName){

        Organization organization = getOrganizationByName(organizationName);
        Project project = getProjectByNameAndOrganizationId(projectName, organization.getId());

        projectRepository.delete(project);
    }


}
