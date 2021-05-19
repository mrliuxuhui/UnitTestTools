package com.flyingwillow.utt.extension.code;

import com.flyingwillow.utt.constant.PhaseNames;
import com.flyingwillow.utt.extensionpoint.code.UttFormatChecker;
import com.intellij.lang.Language;
import com.intellij.lang.StdLanguages;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiDeclarationStatement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpressionStatement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiTreeUtil;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JavaUttFormatChecker implements UttFormatChecker {

    public static final String TEST_FRAMEWORK = "MockitoJUnitRunner.class";

    @Override
    public boolean validateGivenPhase(PsiElement targetMethod, PsiElement testMethod) {
        return false;
    }

    @Override
    public boolean validateWhenPhase(PsiElement targetMethod, PsiElement testMethod) {
        return getWhenStatement(targetMethod, testMethod).isPresent();
    }

    private Optional<PsiElement> getWhenStatement(PsiElement targetMethod, PsiElement testMethod) {

        if (!(targetMethod instanceof PsiMethod) || !(testMethod instanceof PsiMethod)) {
            return Optional.ofNullable(null);
        }
        // not valid
        if (!getUttPhaseElement((PsiMethod) testMethod, PhaseNames.WHEN).isPresent()) {
            return Optional.ofNullable(null);
        }
        final List<PsiMethodCallExpression> methodCallExpressions = PsiTreeUtil.findChildrenOfAnyType(testMethod, PsiMethodCallExpression.class).stream()
                .filter(mc ->
                        mc.resolveMethod().equals(targetMethod)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(methodCallExpressions)) {
            return Optional.ofNullable(null);
        }
        PsiMethod target = (PsiMethod) targetMethod;
        PsiMethodCallExpression mce = methodCallExpressions.get(0);
        if (target.getReturnType() == PsiType.VOID) {
            return Optional.ofNullable(PsiTreeUtil.getParentOfType(mce, PsiExpressionStatement.class));
        } else {
            return Optional.ofNullable(PsiTreeUtil.getParentOfType(mce, PsiDeclarationStatement.class));
        }
    }

    private Optional<PsiComment> getUttPhaseElement(PsiMethod testMethod, String phase) {
        return PsiTreeUtil.findChildrenOfType(testMethod, PsiComment.class)
                .stream().filter(comment -> PhaseNames.patternMap.get(phase).matcher(comment.getText()).matches()).findFirst();
    }

    @Override
    public boolean validateThenPhase(PsiElement targetMethod, PsiElement testMethod) {
        return false;
    }

    @Override
    public Language getLanguage() {
        return StdLanguages.JAVA;
    }

    @Override
    public String getTestFramework() {
        return TEST_FRAMEWORK;
    }

    @Override
    public String getTestFramework(PsiElement element) {
        PsiClass psiClass = element instanceof PsiClass ? (PsiClass) element : PsiTreeUtil.getParentOfType(element, PsiClass.class);
        if (null == psiClass) {
            return "";
        }
        final PsiAnnotation runWith = psiClass.getAnnotation("org.junit.runner.RunWith");
        return runWith.findAttributeValue("value").getText();
    }
}
