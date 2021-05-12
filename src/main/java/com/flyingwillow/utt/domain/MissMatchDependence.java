package com.flyingwillow.utt.domain;

import lombok.Data;

@Data
public class MissMatchDependence {
    private Dependence target;
    private Boolean missed = false;
    private Boolean versionMismatch = false;
    private Boolean scopeMismatch = false;
    private String version;
    private String scope;

    public String getStringMsg() {
        StringBuilder sb = new StringBuilder();
        sb.append(target.getGroupId()).append("-").append(target.getArtifactId()).append(" : ");
        if (missed.booleanValue()) {
            sb.append("Missing!");
        } else if (versionMismatch.booleanValue()) {
            sb.append("version wanted ").append(target.getVersion()).append(" but ").append(version).append(" found!");
        } else if (scopeMismatch.booleanValue()) {
            sb.append("scope wanted ").append(target.getScope()).append(" but ").append(scope).append(" found!");
        }
        return sb.toString();
    }
}
