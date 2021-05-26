package com.flyingwillow.utt.extensionpoint.provider;

import com.flyingwillow.utt.constant.ExtensionPoints;
import com.intellij.lang.Language;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;

/**
 *
 */
public interface UttMethodLineMarkerProvider {

    ExtensionPointName<UttMethodLineMarkerProvider> EXTENSION_POINT_NAME = ExtensionPointName.create(ExtensionPoints.NS + "methodLineMarker");

    Language getLanguage();

    /**
     * whether or not test icon should be shown
     * 1. file not exclude
     * 2. an public method
     * 3. having an associated test method
     * 4. not static method
     *
     * @param element
     */
    boolean shouldShowMarker(@NotNull PsiElement element);

    /**
     * which icon to be shown
     *
     * @return
     */
    Icon getIcon(@NotNull PsiElement element);

    /**
     * location to be associated
     *
     * @return
     */

    List<PsiElement> getAssociate(@NotNull PsiElement element);
}
