package com.task_manager.metadata.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/organizations/{organizationName}/tags")
public class TagController {
    private final TagService tagService;

    @GetMapping
    public ResponseEntity<?> getAllTags(@PathVariable String organizationName){
        return new ResponseEntity<>(tagService.getAllTags(organizationName), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createTag(@PathVariable String organizationName, @RequestBody Tag tag){
        return new ResponseEntity<>(tagService.createTag(organizationName, tag), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTagById(@PathVariable String organizationName, @PathVariable Long id){
        return new ResponseEntity<>(tagService.getTagById(organizationName, id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTag(@PathVariable String organizationName, @PathVariable Long id, @RequestBody Tag tag){
        return new ResponseEntity<>(tagService.updateTag(organizationName, id,tag), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable String organizationName, @PathVariable Long id){
        tagService.deleteTag(organizationName, id);
        return new ResponseEntity<>("Tag was successfully deleted", HttpStatus.OK);
    }
}
