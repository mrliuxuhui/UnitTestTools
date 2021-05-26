package com.flyingwillow.utt.services;

import com.flyingwillow.utt.extensionpoint.code.TestCodeBuilder;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public final class UttCodeBuildService implements UttBaseService {

    private final Map<String, TestCodeBuilder> builderMap = new HashMap<>(20);

    public boolean testFileExist(PsiFile current) {
        return getTestFile(current).isPresent();
    }

    public Optional<PsiFile> getTestFile(PsiFile current) {
        return Optional.ofNullable(null);
    }

    public boolean hoverOnMethod(PsiElement current) {
        return false;
    }

    public boolean hoverOnClass(PsiElement current) {
        return false;
    }

    public PsiFile createTestFile(PsiFile target) {
        return null;
    }

    public PsiElement createTestMethod(PsiElement targetMethod, PsiFile testFile) {

        return null;
    }

    private TestCodeBuilder getCodeBuilder(String key) {
        return builderMap.get(key);
    }

    private String getBuilderKey(Project project) {
        return "";
    }

    @Override
    public void init(Project project) {

    }

    @Override
    public int getOrder() {
        return UttBaseService.LOWEST_ORDER;
    }
}
