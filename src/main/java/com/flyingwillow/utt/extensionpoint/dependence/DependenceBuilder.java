package com.flyingwillow.utt.extensionpoint.dependence;

import com.flyingwillow.utt.domain.Dependence;
import com.flyingwillow.utt.domain.ProjectInfo;
import com.intellij.openapi.extensions.ExtensionPointName;

import java.util.List;

/**
 *
 * */
public interface DependenceBuilder {

    ExtensionPointName<DependenceBuilder> EXTENSION_POINT_NAME = ExtensionPointName.create("DependenceBuilder");

    /**
     *
     * */
    boolean match(ProjectInfo projectInfo);

    List<Dependence> getDependenceList();
}
