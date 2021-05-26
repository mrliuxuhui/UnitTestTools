package com.flyingwillow.utt.services;

import com.flyingwillow.utt.extensionpoint.code.UttFormatChecker;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public final class UttCodeManageService implements UttBaseService {

    private final Map<String, UttFormatChecker> checkerMap = new HashMap<>(10);

    private void initCodeFormatChecker() {
        final List<UttFormatChecker> extensionList = UttFormatChecker.EXTENSION_POINT_NAME.getExtensionList();
        extensionList.forEach(checker -> checkerMap.put(getCheckerKey(checker), checker));
    }

    @NotNull
    private String getCheckerKey(UttFormatChecker checker) {
        StringBuilder sb = new StringBuilder();
        sb.append(checker.getLanguage().getID()).append("-")
                .append(checker.getTestFramework());
        return sb.toString();
    }

    public boolean validateGivenPhase(PsiElement targetMethod, PsiElement testMethod) {
        final UttFormatChecker checker = getUttFormatChecker(targetMethod, testMethod);
        return null != checker && checker.validateGivenPhase(targetMethod, testMethod);
    }

    public boolean validateWhenPhase(PsiElement targetMethod, PsiElement testMethod) {
        final UttFormatChecker checker = getUttFormatChecker(targetMethod, testMethod);
        return null != checker && checker.validateWhenPhase(targetMethod, testMethod);
    }

    private UttFormatChecker getUttFormatChecker(PsiElement targetMethod, PsiElement testMethod) {
        StringBuilder sb = new StringBuilder();
        sb.append(targetMethod.getLanguage().getID()).append("-");
        final PsiClass testClass = PsiTreeUtil.getParentOfType(testMethod, PsiClass.class);
        sb.append(testClass.getAnnotation("org.junit.runner.RunWith").findAttributeValue("value").getText());
        return checkerMap.get(sb.toString());
    }

    public boolean validateThenPhase(PsiElement targetMethod, PsiElement testMethod) {
        final UttFormatChecker checker = getUttFormatChecker(targetMethod, testMethod);
        return null != checker && checker.validateThenPhase(targetMethod, testMethod);
    }

    @Override
    public void init(Project project) {
        // init checkers
        initCodeFormatChecker();
    }

    @Override
    public int getOrder() {
        return UttBaseService.MIDDLE_ORDER;
    }
}
