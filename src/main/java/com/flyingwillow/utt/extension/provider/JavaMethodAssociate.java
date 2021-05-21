package com.flyingwillow.utt.extension.provider;

import com.flyingwillow.utt.extensionpoint.provider.UttMethodAssociate;
import com.flyingwillow.utt.services.UttMethodAssociationService;
import com.intellij.icons.AllIcons;
import com.intellij.lang.Language;
import com.intellij.lang.StdLanguages;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiModifierList;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

public class JavaMethodAssociate implements UttMethodAssociate {
    @Override
    public Language getLanguage() {
        return StdLanguages.JAVA;
    }

    @Override
    public boolean shouldShowMarker(@NotNull PsiElement element) {
        if (element instanceof PsiIdentifier && element.getParent() instanceof PsiMethod) {
            PsiMethod method = (PsiMethod) element.getParent();
            final PsiModifierList modifierList = method.getModifierList();
            return !method.isConstructor() && modifierList.hasModifierProperty(PsiModifier.PUBLIC) && !modifierList.hasModifierProperty(PsiModifier.STATIC);
        }
        return false;
    }

    @Override
    public Icon getIcon(@NotNull PsiElement element) {
        return AllIcons.Nodes.Test;
    }

    @Override
    public List<PsiElement> getAssociate(@NotNull PsiElement element) {
        if(element instanceof PsiIdentifier && element.getParent() instanceof PsiMethod){
            UttMethodAssociationService service = ServiceManager.getService(UttMethodAssociationService.class);
            return service.getAssociations(element);
        }else {
            return Collections.emptyList();
        }
    }
}
