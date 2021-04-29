package com.flyingwillow.utt.extension.dependence;

import com.flyingwillow.utt.domain.Dependence;
import com.flyingwillow.utt.domain.ProjectInfo;
import com.flyingwillow.utt.extensionpoint.dependence.DependenceBuilder;

import java.util.List;

public class SpringWebDependenceBuilder implements DependenceBuilder {
    @Override
    public boolean match(ProjectInfo projectInfo) {
        return false;
    }

    @Override
    public List<Dependence> getDependenceList() {
        return null;
    }
}
