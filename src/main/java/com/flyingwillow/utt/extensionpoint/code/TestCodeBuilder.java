package com.flyingwillow.utt.extensionpoint.code;

import com.flyingwillow.utt.constant.ExtensionPoints;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

import java.util.Optional;

public interface TestCodeBuilder {

    ExtensionPointName<TestCodeBuilder> EXTENSION_POINT_NAME = ExtensionPointName.create(ExtensionPoints.NS + "testCodeBuilder");

    /**
     * supported language
     */
    String getSupportedLanguage();

    /**
     * supported facet
     * spring spring-mvc and so on
     */
    String getSupportedFacet();

    /**
     * supported test framework
     */
    String getSupportedTestFramework();

    /**
     * find test file if exist
     */
    Optional<PsiFile> getTestFile(PsiFile target);

    /**
     * create test file if no exist
     */
    PsiFile createTestFile(PsiFile target);

    /**
     * create test method
     */
    PsiElement createTestMethod(PsiElement targetMethod, PsiFile testFile);

    /**
     * weather hover on class
     */
    boolean hoverOnClass(PsiElement current);

    /**
     * weather hover on method
     */
    boolean hoverOnMethod(PsiElement current);
}
