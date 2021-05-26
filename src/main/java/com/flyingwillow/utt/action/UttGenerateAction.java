package com.flyingwillow.utt.action;

import com.flyingwillow.utt.services.UttCodeBuildService;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.intellij.openapi.actionSystem.CommonDataKeys.PSI_ELEMENT;
import static com.intellij.openapi.actionSystem.CommonDataKeys.PSI_FILE;

public class UttGenerateAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {

        // get current file
        PsiFile file = anActionEvent.getData(PSI_FILE);
        PsiElement current = anActionEvent.getData(PSI_ELEMENT);
        UttCodeBuildService uttCodeBuildService = ServiceManager.getService(UttCodeBuildService.class);

        // current cursor on class
        Optional<PsiFile> testFile = uttCodeBuildService.getTestFile(file);
        if (uttCodeBuildService.hoverOnClass(current)) {
            // prop jump
            // create
            if (!testFile.isPresent()) {
                testFile = Optional.of(uttCodeBuildService.createTestFile(file));
            }
            // create test file and jump to
            if (testFile.isPresent()) {
                testFile.get().navigate(true);
            }

        } else if (uttCodeBuildService.hoverOnMethod(current)) {
            // jump to test class create test method
            final PsiElement testMethod = uttCodeBuildService.createTestMethod(current.getParent(), testFile.get());
            ((NavigationItem) testMethod.getNavigationElement()).navigate(false);
        }

    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        // hover on class or method enabled
        UttCodeBuildService uttCodeBuildService = ServiceManager.getService(UttCodeBuildService.class);
        PsiElement current = e.getData(PSI_ELEMENT);

        if (uttCodeBuildService.hoverOnClass(current) || uttCodeBuildService.hoverOnMethod(current)) {
            e.getPresentation().setEnabledAndVisible(true);
        } else {
            e.getPresentation().setEnabledAndVisible(false);
        }
    }
}
