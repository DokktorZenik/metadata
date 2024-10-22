package com.task_manager.metadata.project.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
public class ProjectsResponse {

    @JsonProperty("_embedded")
    Embedded embedded;

    public ProjectsResponse(List<ProjectResponse> responses) {
        this.embedded = new Embedded(responses);
    }

    @Value
    private static class Embedded {

        private List<ProjectResponse> responses;

        public Embedded(List<ProjectResponse> responses) {
            this.responses = responses;
        }
    }
}
