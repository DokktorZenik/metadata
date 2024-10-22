package com.task_manager.metadata.tag;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagResponse {
    private Long id;
    private Long orgId;
    private JsonNode content;
}