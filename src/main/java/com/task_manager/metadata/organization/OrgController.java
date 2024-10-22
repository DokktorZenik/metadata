package com.task_manager.metadata.organization;

import com.task_manager.metadata.organization.models.OrgCreateRequest;
import com.task_manager.metadata.organization.models.OrgEntity;
import com.task_manager.metadata.organization.models.OrgResponse;
import com.task_manager.metadata.organization.models.OrgUpdateRequest;
import com.task_manager.metadata.organization.models.OrgsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/organizations")
@RequiredArgsConstructor
public class OrgController {
    private final OrgService organizationService;

    @GetMapping
    public ResponseEntity<OrgsResponse> getAllOrganizations(){
        return ResponseEntity.ok(new OrgsResponse(
                organizationService
                        .getAllOrganizations()
                        .stream()
                        .map(OrgEntity::toResponse)
                        .toList()
        ));
    }

    @GetMapping("{name}")
    public ResponseEntity<OrgResponse> getOrganization(@PathVariable("name") String organizationName){
        return ResponseEntity
                .ok(
                organizationService
                        .getOrganizationByName(organizationName)
                        .toResponse());
    }

    @PostMapping
    public ResponseEntity<OrgResponse> createOrganization(@RequestBody OrgCreateRequest organization){
        return ResponseEntity
                .status(201)
                .body(organizationService
                        .createOrganization(organization)
                        .toResponse());
    }

    @PutMapping("{name}")
    public ResponseEntity<OrgResponse> updateOrganization(@PathVariable("name") String organizationName, @RequestBody OrgUpdateRequest updatedOrganization){
        return ResponseEntity.
                ok(organizationService
                        .updateOrganization(organizationName, updatedOrganization)
                        .toResponse());
    }

    @DeleteMapping("{name}")
    public ResponseEntity<String> deleteOrganization(@PathVariable("name") String organizationName){
        organizationService.deleteOrganization(organizationName);
        return new ResponseEntity<>("Organization was successfully deleted",HttpStatus.OK);
    }

}
