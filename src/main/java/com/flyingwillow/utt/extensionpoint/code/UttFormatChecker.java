package com.flyingwillow.utt.extensionpoint.code;

import com.flyingwillow.utt.constant.ExtensionPoints;
import com.intellij.lang.Language;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.psi.PsiElement;

public interface UttFormatChecker {

    ExtensionPointName<UttFormatChecker> EXTENSION_POINT_NAME = ExtensionPointName.create(ExtensionPoints.NS + "uttFormatChecker");

    /**
     * check if this is an valid given phase
     * 1. started with "// given" line
     * 2. condition and parameters build
     * 3. mocks
     */
    boolean validateGivenPhase(PsiElement targetMethod, PsiElement testMethod);

    /**
     * check if this is an valid when phase
     * 1. started with "// when" line
     * 2. a target method called
     * 3. if has return value, a declaration statement
     */
    boolean validateWhenPhase(PsiElement targetMethod, PsiElement testMethod);

    /**
     * check if this is an valid then phase
     * 1. started with "// then" line
     * 2. assertions or verifications
     */
    boolean validateThenPhase(PsiElement targetMethod, PsiElement testMethod);

    /**
     * get supported language
     */
    Language getLanguage();

    /**
     * get supported test framework
     * RunWith(xxxx)
     */
    String getTestFramework();

    /**
     * get test framework from test class
     * RunWith(xx)
     */
    String getTestFramework(PsiElement element);
}
