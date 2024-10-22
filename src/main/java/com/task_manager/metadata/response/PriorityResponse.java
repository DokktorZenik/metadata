package com.task_manager.metadata.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriorityResponse {
    private Long id;
    private Long orgId;
    private JsonNode content;
}
