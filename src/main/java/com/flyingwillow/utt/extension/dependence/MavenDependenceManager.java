package com.flyingwillow.utt.extension.dependence;

import com.flyingwillow.utt.extensionpoint.dependence.DependenceManager;
import com.flyingwillow.utt.domain.Dependence;
import com.flyingwillow.utt.domain.ProjectInfo;

import java.util.List;

public class MavenDependenceManager implements DependenceManager {
    @Override
    public boolean match(ProjectInfo projectInfo) {
        return false;
    }

    @Override
    public void setupIfNecessary(List<Dependence> list) {

    }
}
