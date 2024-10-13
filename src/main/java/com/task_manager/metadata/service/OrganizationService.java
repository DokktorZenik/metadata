package com.task_manager.metadata.service;

import com.task_manager.metadata.entity.Organization;
import com.task_manager.metadata.exception.ResourceNotFoundException;
import com.task_manager.metadata.repository.OrganizationRepository;
import com.task_manager.metadata.request.OrganizationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    public List<Organization> getAllOrganizations(){
       return organizationRepository.findAll();
    }

    public Organization createOrganization(OrganizationRequest organization){
        if(organizationRepository.findByName(organization.getName()).isPresent())
            throw new IllegalArgumentException("Organization with name " + organization.getName() + " already exists");

        return organizationRepository.save(organization.toEntity());
    }

    public Organization getOrganization(String name){
        return organizationRepository.findByName(name)
                .orElseThrow(()->new ResourceNotFoundException("Organization", "name", name));
    }

    public Organization updateOrganization(String name, OrganizationRequest updatedOrganization){
        Organization organization = organizationRepository.findByName(name)
                .orElseThrow(()->new ResourceNotFoundException("Organization", "name", name));

        organization.setName(updatedOrganization.getName());
        organization.setTitle(updatedOrganization.getTitle());

        return organizationRepository.save(organization);
    }

    public void deleteOrganization(String name){
        Organization organization = organizationRepository.findByName(name)
                .orElseThrow(()->new ResourceNotFoundException("Organization", "name", name));

        organizationRepository.delete(organization);
    }

}
