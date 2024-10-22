package com.task_manager.metadata.estimate;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstimateResponse {
    private Long id;
    private Long orgId;
    private JsonNode content;
}
