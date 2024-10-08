package com.task_manager.metadata.controller;

import com.task_manager.metadata.entity.Priority;
import com.task_manager.metadata.entity.Tag;
import com.task_manager.metadata.service.PriorityService;
import com.task_manager.metadata.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/organizations/{organizationName}/projects/{projectName}/priorities")
public class PriorityController {
    private final PriorityService priorityService;

    @GetMapping
    public ResponseEntity<?> getAllPriorities(@PathVariable String organizationName, @PathVariable String projectName){
        return new ResponseEntity<>(priorityService.getAllPriorities(organizationName, projectName), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createPriority(@PathVariable String organizationName, @PathVariable String projectName, @RequestBody Priority priority){
        return new ResponseEntity<>(priorityService.createPriority(organizationName, projectName,priority), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPriorityById(@PathVariable String organizationName,@PathVariable String projectName,@PathVariable Long id){
        return new ResponseEntity<>(priorityService.getPriorityById(organizationName, projectName, id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePriority(@PathVariable String organizationName,@PathVariable String projectName,@PathVariable Long id, @RequestBody Priority priority){
        return new ResponseEntity<>(priorityService.updatePriority(organizationName, projectName,id,priority), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePriority(@PathVariable String organizationName,@PathVariable String projectName,@PathVariable Long id){
        priorityService.deletePriority(organizationName, projectName, id);
        return new ResponseEntity<>("Priority was successfully deleted", HttpStatus.OK);
    }
}
