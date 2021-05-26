package com.flyingwillow.utt.extensionpoint.cache;

import com.flyingwillow.utt.constant.ExtensionPoints;
import com.intellij.lang.Language;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;

import java.util.List;
import java.util.Map;

/**
 * target method and test method association map
 */
public interface MethodAssociationCacheBuilder {

    ExtensionPointName<MethodAssociationCacheBuilder> EXTENSION_POINT_NAME = ExtensionPointName.create(ExtensionPoints.NS + "methodAssociationBuilder");

    /**
     * build key
     */
    String getUnifiedKey(PsiElement element);

    /**
     * load all test methods
     */
    Map<String, List<PsiElement>> loadMap(Project project);

    /**
     * search association in test class
     */
    List<PsiElement> getAssociate(PsiElement element);

    /**
     * supported language
     */
    Language getLanguage();
}
