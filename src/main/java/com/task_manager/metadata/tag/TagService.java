package com.task_manager.metadata.tag;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_manager.metadata.organization.models.OrgEntity;
import com.task_manager.metadata.exception.ResourceNotFoundException;
import com.task_manager.metadata.organization.OrgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final OrgRepository orgRepository;
    private final ObjectMapper objectMapper;

    private TagResponse mapToTagResponse(Tag tag) {
        JsonNode content = objectMapper.createObjectNode()
                .put("title", tag.getTitle())
                .put("color", tag.getColor());

        return new TagResponse(tag.getId(),tag.getOrganizationId(), content);
    }

    private OrgEntity getOrganizationByName(String organizationName) {
        return orgRepository.findByName(organizationName)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "name", organizationName));
    }

    public TagResponse createTag(String organizationName, Tag tag){
        OrgEntity orgEntity = getOrganizationByName(organizationName);

        tag.setOrganizationId(orgEntity.getId());

        return mapToTagResponse(tagRepository.save(tag));
    }

    public List<TagResponse> getAllTags(String organizationName){
        OrgEntity orgEntity = getOrganizationByName(organizationName);

        return tagRepository.findByOrganizationId(orgEntity.getId())
                .stream().map(tag -> mapToTagResponse(tag)).toList();
    }

    public TagResponse getTagById(String organizationName, Long id){
        OrgEntity orgEntity = getOrganizationByName(organizationName);

        return mapToTagResponse(tagRepository.findByIdAndOrganizationId(id, orgEntity.getId())
                .orElseThrow(()->new ResourceNotFoundException("Tag", "id", id)));
    }

    public TagResponse updateTag(String organizationName, Long id, Tag tag){
        OrgEntity orgEntity = getOrganizationByName(organizationName);

        Tag updatedTag = tagRepository.findByIdAndOrganizationId(id, orgEntity.getId())
                .orElseThrow(()->new ResourceNotFoundException("Tag", "id", id));

        updatedTag.setTitle(tag.getTitle());
        updatedTag.setColor(tag.getColor());

        return mapToTagResponse(tagRepository.save(updatedTag));
    }

    public void deleteTag(String organizationName, Long id){
        OrgEntity orgEntity = getOrganizationByName(organizationName);

        Tag tag = tagRepository.findByIdAndOrganizationId(id, orgEntity.getId())
                .orElseThrow(()->new ResourceNotFoundException("Tag", "id", id));

        tagRepository.delete(tag);
    }
}
