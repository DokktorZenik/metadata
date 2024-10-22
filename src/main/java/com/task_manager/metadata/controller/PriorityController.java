package com.task_manager.metadata.controller;

import com.task_manager.metadata.entity.Priority;
import com.task_manager.metadata.service.PriorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/organizations/{organizationName}/priorities")
public class PriorityController {
    private final PriorityService priorityService;

    @GetMapping
    public ResponseEntity<?> getAllPriorities(@PathVariable String organizationName){
        return new ResponseEntity<>(priorityService.getAllPriorities(organizationName), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createPriority(@PathVariable String organizationName, @RequestBody Priority priority){
        return new ResponseEntity<>(priorityService.createPriority(organizationName, priority), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPriorityById(@PathVariable String organizationName, @PathVariable Long id){
        return new ResponseEntity<>(priorityService.getPriorityById(organizationName, id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePriority(@PathVariable String organizationName, @PathVariable Long id, @RequestBody Priority priority){
        return new ResponseEntity<>(priorityService.updatePriority(organizationName, id, priority), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePriority(@PathVariable String organizationName, @PathVariable Long id){
        priorityService.deletePriority(organizationName, id);
        return new ResponseEntity<>("Priority was successfully deleted", HttpStatus.OK);
    }
}
