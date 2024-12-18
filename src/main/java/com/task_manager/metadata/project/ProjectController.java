package com.task_manager.metadata.project;

import com.fasterxml.jackson.databind.node.ObjectNode;
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
@RequestMapping("v1/organizations/{name}")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/projects")
    public ResponseEntity<ProjectsResponse> getAllProjectsByOrganizationId(@PathVariable("name") String organizationName){
        return ResponseEntity
                .ok(new ProjectsResponse(
                        projectService
                        .getAllProjectsByOrganizationName(organizationName)
                                .stream()
                                .map(ProjectEntity::toResponse)
                                .toList()));
    }

    @GetMapping("/projects/user/{userId}")
    public ResponseEntity<ProjectsResponse> getProjectByOrganizationNameAndUserId(@PathVariable("name") String organizationName,
                                                                                 @PathVariable("userId") Long userId){
        return ResponseEntity
                .ok(new ProjectsResponse(
                        projectService
                                .getAllProjectsByOrganizationNameAndUserId(organizationName,userId)
                                .stream()
                                .map(ProjectEntity::toResponse)
                                .toList()));
    }

    @PostMapping("/projects")
    public ResponseEntity<ProjectResponse> createProject(@PathVariable("name") String organizationName, @RequestBody ProjectCreateRequest project){
        return ResponseEntity
                .status(201)
                .body(projectService.createProject(organizationName, project).toResponse());
    }

    @GetMapping("/projects/{projectName}")
    public ResponseEntity<ProjectResponse> getProjectByName(@PathVariable String name,@PathVariable String projectName){
        return ResponseEntity.ok(projectService
                .getProjectByName(name,projectName)
                .toResponse());
    }

    @PutMapping("/projects/{projectName}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable String name,@PathVariable String projectName, @RequestBody ProjectUpdateRequest project){
        return ResponseEntity.ok(
                projectService
                        .updateProject(name,projectName, project)
                        .toResponse());
    }

    @DeleteMapping("/projects/{projectName}")
    public ResponseEntity<String> deleteProject(@PathVariable String name,@PathVariable String projectName){
        projectService.deleteProject(name,projectName);
        return new ResponseEntity<>("Project was successfully deleted", HttpStatus.OK);
    }
}
