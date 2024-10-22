package com.task_manager.metadata.service;

import com.github.slugify.Slugify;
import com.task_manager.metadata.entity.Organization;
import com.task_manager.metadata.exception.ResourceNotFoundException;
import com.task_manager.metadata.repository.OrganizationRepository;
import com.task_manager.metadata.request.CreateOrganizationRequest;
import com.task_manager.metadata.request.UpdateOrganizationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final static Slugify slugify = new Slugify().withUnderscoreSeparator(true).withCustomReplacement("-", "_");

    private Organization getOrganizationByName(String organizationName) {
        return organizationRepository.findByName(organizationName)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "name", organizationName));
    }

    public List<Organization> getAllOrganizations(){
       return organizationRepository.findAll();
    }

    public Organization createOrganization(CreateOrganizationRequest organization){

        if(organizationRepository.findByName(organization.getTitle()).isPresent())
            throw new IllegalArgumentException("Organization with name " + organization.getTitle() + " already exists");

        organization.setName(slugify.slugify(organization.getTitle()));

        return organizationRepository.save(organization.toEntity());
    }

    public Organization getOrganization(String name){
        return getOrganizationByName(name);
    }

    public Organization updateOrganization(String name, UpdateOrganizationRequest updatedOrganization){
        Organization organization = getOrganizationByName(name);

        organization.setTitle(updatedOrganization.getTitle());
        organization.setName(slugify.slugify(updatedOrganization.getTitle()));

        return organizationRepository.save(organization);
    }

    public void deleteOrganization(String name){
        Organization organization = getOrganizationByName(name);

        organizationRepository.delete(organization);
    }

}
