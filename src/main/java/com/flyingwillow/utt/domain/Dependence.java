package com.flyingwillow.utt.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Dependence {
    private String groupId;
    private String artifactId;
    private String version;
    private String scope;
}
