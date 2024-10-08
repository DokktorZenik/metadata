package com.task_manager.metadata.controller;

import com.task_manager.metadata.entity.Tag;
import com.task_manager.metadata.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/organizations/{organizationName}/projects/{projectName}/tags")
public class TagController {
    private final TagService tagService;

    @GetMapping
    public ResponseEntity<?> getAllTags(@PathVariable String organizationName,@PathVariable String projectName){
        return new ResponseEntity<>(tagService.getAllTags(organizationName, projectName), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createTag(@PathVariable String organizationName,@PathVariable String projectName, @RequestBody Tag tag){
        return new ResponseEntity<>(tagService.createTag(organizationName, projectName,tag), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTagById(@PathVariable String organizationName,@PathVariable String projectName,@PathVariable Long id){
        return new ResponseEntity<>(tagService.getTagById(organizationName, projectName, id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTag(@PathVariable String organizationName,@PathVariable String projectName,@PathVariable Long id, @RequestBody Tag tag){
        return new ResponseEntity<>(tagService.updateTag(organizationName, projectName,id,tag), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable String organizationName,@PathVariable String projectName,@PathVariable Long id){
        tagService.deleteTag(organizationName, projectName, id);
        return new ResponseEntity<>("Tag was successfully deleted", HttpStatus.OK);
    }
}
