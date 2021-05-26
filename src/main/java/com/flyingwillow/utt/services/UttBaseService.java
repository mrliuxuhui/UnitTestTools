package com.flyingwillow.utt.services;

import com.intellij.openapi.project.Project;

public interface UttBaseService {

    int HIGHEST_ORDER = Integer.MIN_VALUE;
    int MIDDLE_ORDER = 0;
    int LOWEST_ORDER = Integer.MAX_VALUE;

    void init(Project project);

    int getOrder();

}
