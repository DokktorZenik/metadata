package com.task_manager.metadata.controller;

import com.task_manager.metadata.entity.Project;
import com.task_manager.metadata.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/organizations")
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping("{name}/projects")
    public ResponseEntity<?> getAllProjectsByOrganizationName(@PathVariable("name") String organizationName){
        return new ResponseEntity<>(projectService.getAllProjectsByOrganizationName(organizationName), HttpStatus.OK);
    }

    @PostMapping("{name}/projects")
    public ResponseEntity<?> createProject(@PathVariable("name") String organizationName,@RequestBody Project project){
        return new ResponseEntity<>(projectService.createProject(organizationName, project), HttpStatus.CREATED);
    }

    @GetMapping("{organizationName}/projects/{projectName}")
    public ResponseEntity<?> getProjectByName(@PathVariable String organizationName,@PathVariable String projectName){
        return new ResponseEntity<>(projectService.getProjectByName(organizationName,projectName),HttpStatus.OK);
    }

    @PutMapping("{organizationName}/projects/{projectName}")
    public ResponseEntity<?> updateProject(@PathVariable String organizationName,@PathVariable String projectName, @RequestBody Project project){
        return new ResponseEntity<>(projectService.updateProject(organizationName,projectName, project), HttpStatus.OK);
    }

    @DeleteMapping("{organizationName}/projects/{projectName}")
    public ResponseEntity<?> deleteProject(@PathVariable String organizationName,@PathVariable String projectName){
        projectService.deleteProject(organizationName,projectName);
        return new ResponseEntity<>("Project was successfully deleted", HttpStatus.OK);
    }
}
