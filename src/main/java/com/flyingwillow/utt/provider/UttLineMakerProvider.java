package com.flyingwillow.utt.provider;

import com.flyingwillow.utt.extensionpoint.provider.UttMethodAssociate;
import com.flyingwillow.utt.services.ProjectInfoService;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class UttLineMakerProvider extends RelatedItemLineMarkerProvider {

    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element, @NotNull Collection<? super RelatedItemLineMarkerInfo<?>> result) {

        //
        final ProjectInfoService projectInfoService = ServiceManager.getService(ProjectInfoService.class);
        final UttMethodAssociate methodAssociate = projectInfoService.getMethodAssociate(element.getLanguage());

        // fail fast
        if (null == methodAssociate) {
            return;
        }

        if (methodAssociate.shouldShowMarker(element)) {
            final NavigationGutterIconBuilder<PsiElement> jumpToTestMethod = NavigationGutterIconBuilder.create(methodAssociate.getIcon(element))
                    .setTooltipTitle("Jump to Test Method")
                    .setTargets(methodAssociate.getAssociate(element));
            result.add(jumpToTestMethod.createLineMarkerInfo(element));
        }

    }
}
