package com.task_manager.metadata.organization.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
public class OrgsResponse {

    @JsonProperty("_embedded")
    Embedded embedded;

    public OrgsResponse(List<OrgResponse> responses) {
        this.embedded = new Embedded(responses);
    }

    @Value
    private static class Embedded {

        private List<OrgResponse> responses;

        public Embedded(List<OrgResponse> responses) {
            this.responses = responses;
        }
    }
}
