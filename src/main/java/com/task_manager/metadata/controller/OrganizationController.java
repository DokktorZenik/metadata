package com.task_manager.metadata.controller;

import com.task_manager.metadata.entity.Organization;
import com.task_manager.metadata.request.OrganizationRequest;
import com.task_manager.metadata.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;

    @GetMapping
    public ResponseEntity<?> getAllOrganizations(){
        return new ResponseEntity<>(organizationService.getAllOrganizations(), HttpStatus.OK);
    }

    @GetMapping("{name}")
    public ResponseEntity<?> getOrganization(@PathVariable("name") String organizationName){
        return new ResponseEntity<>(organizationService.getOrganization(organizationName), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createOrganization(@RequestBody OrganizationRequest organization){
        return new ResponseEntity<>(organizationService.createOrganization(organization), HttpStatus.CREATED);
    }

    @PutMapping("{name}")
    public ResponseEntity<?> updateOrganization(@PathVariable("name") String organizationName, @RequestBody OrganizationRequest updatedOrganization){
        return new ResponseEntity<>(organizationService.updateOrganization(organizationName, updatedOrganization), HttpStatus.OK);
    }

    @DeleteMapping("{name}")
    public ResponseEntity<?> deleteOrganization(@PathVariable("name") String organizationName){
        organizationService.deleteOrganization(organizationName);
        return new ResponseEntity<>("Organization was successfully deleted",HttpStatus.OK);
    }

}
