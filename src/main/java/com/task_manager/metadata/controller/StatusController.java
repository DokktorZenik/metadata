package com.task_manager.metadata.controller;

import com.task_manager.metadata.entity.Status;
import com.task_manager.metadata.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/organizations/{organizationName}/projects/{projectName}/statuses")
public class StatusController {
    private final StatusService statusService;

    @GetMapping
    public ResponseEntity<?> getAllStatuses(@PathVariable String organizationName, @PathVariable String projectName){
        return new ResponseEntity<>(statusService.getAllStatuses(organizationName, projectName), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createStatus(@PathVariable String organizationName, @PathVariable String projectName, @RequestBody Status status){
        return new ResponseEntity<>(statusService.createStatus(organizationName, projectName,status), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStatusById(@PathVariable String organizationName,@PathVariable String projectName,@PathVariable Long id){
        return new ResponseEntity<>(statusService.getStatusById(organizationName, projectName, id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable String organizationName,@PathVariable String projectName,@PathVariable Long id, @RequestBody Status status){
        return new ResponseEntity<>(statusService.updateStatus(organizationName, projectName,id,status), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStatus(@PathVariable String organizationName,@PathVariable String projectName,@PathVariable Long id){
        statusService.deleteStatus(organizationName, projectName, id);
        return new ResponseEntity<>("Priority was successfully deleted", HttpStatus.OK);
    }
}
