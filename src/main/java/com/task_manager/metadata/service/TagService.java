package com.task_manager.metadata.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_manager.metadata.entity.Organization;
import com.task_manager.metadata.entity.Tag;
import com.task_manager.metadata.exception.ResourceNotFoundException;
import com.task_manager.metadata.repository.OrganizationRepository;
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
    private final ObjectMapper objectMapper;

    private TagResponse mapToTagResponse(Tag tag) {
        JsonNode content = objectMapper.createObjectNode()
                .put("title", tag.getTitle())
                .put("color", tag.getColor());

        return new TagResponse(tag.getId(),tag.getOrganizationId(), content);
    }

    private Organization getOrganizationByName(String organizationName) {
        return organizationRepository.findByName(organizationName)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "name", organizationName));
    }

    public TagResponse createTag(String organizationName, Tag tag){
        Organization organization = getOrganizationByName(organizationName);

        tag.setOrganizationId(organization.getId());

        return mapToTagResponse(tagRepository.save(tag));
    }

    public List<TagResponse> getAllTags(String organizationName){
        Organization organization = getOrganizationByName(organizationName);

        return tagRepository.findByOrganizationId(organization.getId())
                .stream().map(tag -> mapToTagResponse(tag)).toList();
    }

    public TagResponse getTagById(String organizationName, Long id){
        Organization organization = getOrganizationByName(organizationName);

        return mapToTagResponse(tagRepository.findByIdAndOrganizationId(id, organization.getId())
                .orElseThrow(()->new ResourceNotFoundException("Tag", "id", id)));
    }

    public TagResponse updateTag(String organizationName, Long id, Tag tag){
        Organization organization = getOrganizationByName(organizationName);

        Tag updatedTag = tagRepository.findByIdAndOrganizationId(id, organization.getId())
                .orElseThrow(()->new ResourceNotFoundException("Tag", "id", id));

        updatedTag.setTitle(tag.getTitle());
        updatedTag.setColor(tag.getColor());

        return mapToTagResponse(tagRepository.save(updatedTag));
    }

    public void deleteTag(String organizationName, Long id){
        Organization organization = getOrganizationByName(organizationName);

        Tag tag = tagRepository.findByIdAndOrganizationId(id, organization.getId())
                .orElseThrow(()->new ResourceNotFoundException("Tag", "id", id));

        tagRepository.delete(tag);
    }
}
