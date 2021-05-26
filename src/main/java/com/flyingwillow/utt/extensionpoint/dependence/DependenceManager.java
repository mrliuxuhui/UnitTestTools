package com.flyingwillow.utt.extensionpoint.dependence;

import com.flyingwillow.utt.constant.ExtensionPoints;
import com.flyingwillow.utt.domain.Dependence;
import com.flyingwillow.utt.domain.MissMatchDependence;
import com.flyingwillow.utt.domain.ProjectInfo;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.project.Project;

import java.util.List;

/**
 * check dependencies and write the dependencies to the dependence manage file
 * such as gradle or maven
 */
public interface DependenceManager {

    ExtensionPointName<DependenceManager> EXTENSION_POINT_NAME = ExtensionPointName.create(ExtensionPoints.NS + "dependenceManager");

    /**
     *
     */
    boolean match(ProjectInfo projectInfo);

    /**
     *
     */
    void setupIfNecessary(List<Dependence> list, Project project);

    List<MissMatchDependence> checkDependencies(List<Dependence> list, Project project);
}
