package com.task_manager.metadata.estimate;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/organizations/{organizationName}/estimates")
public class EstimateController {
    private final EstimateService estimateService;

    @GetMapping
    public ResponseEntity<?> getAllEstimates(@PathVariable String organizationName){
        return new ResponseEntity<>(estimateService.getAllEstimates(organizationName), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createEstimate(@PathVariable String organizationName, @RequestBody Estimate estimate){
        return new ResponseEntity<>(estimateService.createEstimate(organizationName,estimate), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEstimateById(@PathVariable String organizationName, @PathVariable Long id){
        return new ResponseEntity<>(estimateService.getEstimateById(organizationName, id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEstimate(@PathVariable String organizationName, @PathVariable Long id, @RequestBody Estimate estimate){
        return new ResponseEntity<>(estimateService.updateEstimate(organizationName, id, estimate), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEstimate(@PathVariable String organizationName, @PathVariable Long id){
        estimateService.deleteEstimate(organizationName, id);
        return new ResponseEntity<>("Estimate was successfully deleted", HttpStatus.OK);
    }
}
