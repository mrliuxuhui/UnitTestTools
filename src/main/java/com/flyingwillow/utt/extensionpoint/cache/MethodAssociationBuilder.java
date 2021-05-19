package com.flyingwillow.utt.extensionpoint.cache;

import com.intellij.lang.Language;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;

import java.util.List;
import java.util.Map;

public interface MethodAssociationBuilder {

    ExtensionPointName<MethodAssociationBuilder> EXTENSION_POINT_NAME = ExtensionPointName.create("com.flyingwillow.utt.methodAssociationBuilder");

    /**
     *  build key
     * */
    String getUnifiedKey(PsiElement element);

    /**
     * load all test methods
     * */
    Map<String, List<PsiElement>> loadMap(Project project);

    /**
     *  search association in test class
     * */
    List<PsiElement> getAssociate(PsiElement element);

    /**
     * supported language
     * */
    Language getLanguage();
}
