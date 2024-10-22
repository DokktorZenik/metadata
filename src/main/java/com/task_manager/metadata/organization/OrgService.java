package com.task_manager.metadata.organization;

import com.github.slugify.Slugify;
import com.task_manager.metadata.exception.ResourceNotFoundException;
import com.task_manager.metadata.organization.models.OrgCreateRequest;
import com.task_manager.metadata.organization.models.OrgEntity;
import com.task_manager.metadata.organization.models.OrgUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrgService {

    private final OrgRepository orgRepository;

    private final static Slugify slugify = new Slugify().withUnderscoreSeparator(true).withCustomReplacement("-", "_");

    public OrgEntity getOrganizationByName(String organizationName) {
        return orgRepository.findByName(organizationName)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "name", organizationName));
    }

    public Long getOrgIdByName(String organizationName) {
        Optional<Long> idByName = orgRepository.getIdByName(organizationName);

        if(idByName.isPresent()) {
            return idByName.get();
        } else {
            throw new ResourceNotFoundException("Organization", "name", organizationName);
        }
    }

    public List<OrgEntity> getAllOrganizations(){
       return orgRepository.findAll();
    }

    public OrgEntity createOrganization(OrgCreateRequest request){

        if(orgRepository.findByName(request.getTitle()).isPresent())
            throw new IllegalArgumentException("Organization with name %s already exists".formatted(request.getTitle()));

        String name = slugify.slugify(request.getTitle());

        return orgRepository.save(
                request.toEntity(name));
    }

    public OrgEntity updateOrganization(String oldName, OrgUpdateRequest updatedOrganization){

        String title = updatedOrganization.getTitle();

        String slugifyTitle = slugify.slugify(title);

        return orgRepository.updateOrg(oldName, title, slugifyTitle);
    }

    public void deleteOrganization(String name){
        orgRepository.deleteByName(name);
    }

}
