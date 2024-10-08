package com.task_manager.metadata.service;

import com.task_manager.metadata.entity.Organization;
import com.task_manager.metadata.entity.Project;
import com.task_manager.metadata.entity.User;
import com.task_manager.metadata.exception.ResourceNotFoundException;
import com.task_manager.metadata.repository.OrganizationRepository;
import com.task_manager.metadata.repository.ProjectRepository;
import com.task_manager.metadata.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final OrganizationRepository organizationRepository;
    private final UserRepository usersRepository;

    public List<Project> getAllProjectsByOrganizationName(String organizationName){
        //TODO check if user in organization
        if(organizationRepository.findByName(organizationName).isPresent())
            return projectRepository.findByOrganizationName(organizationName);
        else
            throw new ResourceNotFoundException("Organization","name", organizationName);
    }

    public Project createProject(String organizationName, Project project){
        Organization organization = organizationRepository.findByName(organizationName)
                .orElseThrow(() -> new ResourceNotFoundException("Organization","name", organizationName));

        User owner = usersRepository.findById(project.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("Such user does not exist"));

        project.setOwnerId(owner.getId());
        project.setOrganizationId(organization.getId());

        return projectRepository.save(project);
    }

    public Project getProjectByName(String organizationName, String projectName){
        Organization organization = organizationRepository.findByName(organizationName)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "name", projectName));

        Project project = projectRepository.findByNameAndOrganizationId(projectName, organization.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Project", "name", projectName));

        return project;
    }

    public Project updateProject(String organizationName, String projectName, Project newProject){
        Organization organization = organizationRepository.findByName(organizationName)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "name", projectName));

        Project project = projectRepository.findByNameAndOrganizationId(projectName, organization.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Project", "name", projectName));

        project.setName(newProject.getName());
        project.setTitle(newProject.getTitle());

        return projectRepository.save(project);
    }

    public void deleteProject(String organizationName, String projectName){
        Organization organization = organizationRepository.findByName(organizationName)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "name", projectName));

        Project project = projectRepository.findByNameAndOrganizationId(projectName, organization.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Project", "name", projectName));

        projectRepository.delete(project);
    }


}
