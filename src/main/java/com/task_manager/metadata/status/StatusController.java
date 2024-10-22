package com.task_manager.metadata.status;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/organizations/{organizationName}/statuses")
public class StatusController {
    private final StatusService statusService;

    @GetMapping
    public ResponseEntity<?> getAllStatuses(@PathVariable String organizationName){
        return new ResponseEntity<>(statusService.getAllStatuses(organizationName), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createStatus(@PathVariable String organizationName, @RequestBody Status status){
        return new ResponseEntity<>(statusService.createStatus(organizationName, status), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStatusById(@PathVariable String organizationName, @PathVariable Long id){
        return new ResponseEntity<>(statusService.getStatusById(organizationName, id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable String organizationName, @PathVariable Long id, @RequestBody Status status){
        return new ResponseEntity<>(statusService.updateStatus(organizationName, id, status), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStatus(@PathVariable String organizationName, @PathVariable Long id){
        statusService.deleteStatus(organizationName, id);
        return new ResponseEntity<>("Priority was successfully deleted", HttpStatus.OK);
    }
}
