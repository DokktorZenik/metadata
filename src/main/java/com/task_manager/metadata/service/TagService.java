package com.task_manager.metadata.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_manager.metadata.entity.Organization;
import com.task_manager.metadata.entity.Project;
import com.task_manager.metadata.entity.Tag;
import com.task_manager.metadata.exception.ResourceNotFoundException;
import com.task_manager.metadata.repository.OrganizationRepository;
import com.task_manager.metadata.repository.ProjectRepository;
import com.task_manager.metadata.repository.TagRepository;
import com.task_manager.metadata.response.TagResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final OrganizationRepository organizationRepository;
    private final ProjectRepository projectRepository;
    private final ObjectMapper objectMapper;

    private TagResponse mapToTagResponse(Tag tag) {
        JsonNode content = objectMapper.createObjectNode()
                .put("title", tag.getTitle())
                .put("color", tag.getColor());

        return new TagResponse(tag.getId(),tag.getOrganizationId(), tag.getProjectId(), content);
    }

    private Organization getOrganizationByName(String organizationName) {
        return organizationRepository.findByName(organizationName)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "name", organizationName));
    }

    private Project getProjectByNameAndOrganizationId(String projectName,Long id) {
        return projectRepository.findByNameAndOrganizationId(projectName,id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "name", projectName));
    }

    public TagResponse createTag(String organizationName, String projectName, Tag tag){
        Organization organization = getOrganizationByName(organizationName);
        Project project = getProjectByNameAndOrganizationId(projectName, organization.getId());

        tag.setOrganizationId(organization.getId());
        tag.setProjectId(project.getId());

        return mapToTagResponse(tagRepository.save(tag));
    }

    public List<TagResponse> getAllTags(String organizationName, String projectName){
        Organization organization = getOrganizationByName(organizationName);

        Project project = getProjectByNameAndOrganizationId(projectName, organization.getId());

        return tagRepository.findByProjectId(project.getId())
                .stream().map(tag -> mapToTagResponse(tag)).toList();
    }

    public TagResponse getTagById(String organizationName, String projectName, Long id){
        Organization organization = getOrganizationByName(organizationName);

        Project project = getProjectByNameAndOrganizationId(projectName, organization.getId());

        return mapToTagResponse(tagRepository.findByIdAndProjectId(id,project.getId())
                .orElseThrow(()->new ResourceNotFoundException("Tag", "id", id)));
    }

    public TagResponse updateTag(String organizationName, String projectName, Long id, Tag tag){
        Organization organization = getOrganizationByName(organizationName);

        Project project = getProjectByNameAndOrganizationId(projectName, organization.getId());

        Tag updatedTag = tagRepository.findByIdAndProjectId(id, project.getId())
                .orElseThrow(()->new ResourceNotFoundException("Tag", "id", id));

        updatedTag.setTitle(tag.getTitle());
        updatedTag.setColor(tag.getColor());

        return mapToTagResponse(tagRepository.save(updatedTag));
    }

    public void deleteTag(String organizationName, String projectName, Long id){
        Organization organization = getOrganizationByName(organizationName);

        Project project = getProjectByNameAndOrganizationId(projectName, organization.getId());

        Tag tag = tagRepository.findByIdAndProjectId(id,project.getId())
                .orElseThrow(()->new ResourceNotFoundException("Tag", "id", id));

        tagRepository.delete(tag);
    }
}
