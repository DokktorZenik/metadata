package com.task_manager.metadata.controller;

import com.task_manager.metadata.entity.Estimate;
import com.task_manager.metadata.service.EstimateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/organizations/{organizationName}/projects/{projectName}/estimates")
public class EstimateController {
    private final EstimateService estimateService;

    @GetMapping
    public ResponseEntity<?> getAllEstimates(@PathVariable String organizationName, @PathVariable String projectName){
        return new ResponseEntity<>(estimateService.getAllEstimates(organizationName, projectName), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createEstimate(@PathVariable String organizationName, @PathVariable String projectName, @RequestBody Estimate estimate){
        return new ResponseEntity<>(estimateService.createEstimate(organizationName, projectName,estimate), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEstimateById(@PathVariable String organizationName,@PathVariable String projectName,@PathVariable Long id){
        return new ResponseEntity<>(estimateService.getEstimateById(organizationName, projectName, id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEstimate(@PathVariable String organizationName,@PathVariable String projectName,@PathVariable Long id, @RequestBody Estimate estimate){
        return new ResponseEntity<>(estimateService.updateEstimate(organizationName, projectName,id,estimate), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEstimate(@PathVariable String organizationName,@PathVariable String projectName,@PathVariable Long id){
        estimateService.deleteEstimate(organizationName, projectName, id);
        return new ResponseEntity<>("Estimate was successfully deleted", HttpStatus.OK);
    }
}
