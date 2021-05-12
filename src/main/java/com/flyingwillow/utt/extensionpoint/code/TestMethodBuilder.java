package com.flyingwillow.utt.extensionpoint.code;

import com.flyingwillow.utt.domain.ProjectInfo;
import com.flyingwillow.utt.extensionpoint.dependence.DependenceBuilder;
import com.intellij.openapi.extensions.ExtensionPointName;

public interface TestMethodBuilder {

    ExtensionPointName<DependenceBuilder> EXTENSION_POINT_NAME = ExtensionPointName.create("testMethodBuilder");

    boolean match(ProjectInfo projectInfo);

    String build();
}
