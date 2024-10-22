package com.task_manager.metadata.project;

import com.task_manager.metadata.project.models.ProjectCreateRequest;
import com.task_manager.metadata.project.models.ProjectEntity;
import com.task_manager.metadata.project.models.ProjectResponse;
import com.task_manager.metadata.project.models.ProjectUpdateRequest;
import com.task_manager.metadata.project.models.ProjectsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/organizations")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("{name}/projects")
    public ResponseEntity<ProjectsResponse> getAllProjectsByOrganizationName(@PathVariable("name") String organizationName){
        return ResponseEntity
                .ok(new ProjectsResponse(
                        projectService
                        .getAllProjectsByOrganizationName(organizationName)
                                .stream()
                                .map(ProjectEntity::toResponse)
                                .toList()));
    }

    @PostMapping("{name}/projects")
    public ResponseEntity<ProjectResponse> createProject(@PathVariable("name") String organizationName, @RequestBody ProjectCreateRequest project){
        return ResponseEntity
                .status(201)
                .body(projectService.createProject(organizationName, project).toResponse());
    }

    @GetMapping("{organizationName}/projects/{projectName}")
    public ResponseEntity<ProjectResponse> getProjectByName(@PathVariable String organizationName,@PathVariable String projectName){
        return ResponseEntity.ok(projectService
                .getProjectByName(organizationName,projectName)
                .toResponse());
    }

    @PutMapping("{organizationName}/projects/{projectName}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable String organizationName,@PathVariable String projectName, @RequestBody ProjectUpdateRequest project){
        return ResponseEntity.ok(
                projectService
                        .updateProject(organizationName,projectName, project)
                        .toResponse());
    }

    @DeleteMapping("{organizationName}/projects/{projectName}")
    public ResponseEntity<String> deleteProject(@PathVariable String organizationName,@PathVariable String projectName){
        projectService.deleteProject(organizationName,projectName);
        return new ResponseEntity<>("Project was successfully deleted", HttpStatus.OK);
    }
}
