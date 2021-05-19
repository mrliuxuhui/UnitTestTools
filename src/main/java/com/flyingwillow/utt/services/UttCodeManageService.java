package com.flyingwillow.utt.services;

import com.flyingwillow.utt.extensionpoint.code.UttFormatChecker;
import com.intellij.openapi.components.Service;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public final class UttCodeManageService {

    private final Map<String, UttFormatChecker> checkerMap = new HashMap<>(10);

    public void initialize() {

        // init checkers
        initCodeFormatChecker();

    }

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
        return null == checker ? false : checker.validateGivenPhase(targetMethod, testMethod);
    }

    public boolean validateWhenPhase(PsiElement targetMethod, PsiElement testMethod) {
        final UttFormatChecker checker = getUttFormatChecker(targetMethod, testMethod);
        return null == checker ? false : checker.validateWhenPhase(targetMethod, testMethod);
    }

    private UttFormatChecker getUttFormatChecker(PsiElement targetMethod, PsiElement testMethod) {
        StringBuilder sb = new StringBuilder();
        sb.append(targetMethod.getLanguage().getID()).append("-");
        final PsiClass testClass = PsiTreeUtil.getParentOfType(testMethod, PsiClass.class);
        sb.append(testClass.getAnnotation("org.junit.runner.RunWith").findAttributeValue("value").getText());
        final UttFormatChecker checker = checkerMap.get(sb.toString());
        return checker;
    }

    public boolean validateThenPhase(PsiElement targetMethod, PsiElement testMethod) {
        final UttFormatChecker checker = getUttFormatChecker(targetMethod, testMethod);
        return null == checker ? false : checker.validateThenPhase(targetMethod, testMethod);
    }
}
