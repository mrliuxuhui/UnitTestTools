package com.flyingwillow.utt.provider;

import com.flyingwillow.utt.extensionpoint.provider.UttMethodLineMarkerProvider;
import com.flyingwillow.utt.services.UttMethodAssociationService;
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
        final UttMethodAssociationService methodAssociationService = ServiceManager.getService(UttMethodAssociationService.class);
        final UttMethodLineMarkerProvider methodLineMarkerProvider = methodAssociationService.getMethodAssociate(element.getLanguage());

        // fail fast
        if (null == methodLineMarkerProvider) {
            return;
        }

        if (methodLineMarkerProvider.shouldShowMarker(element)) {
            final NavigationGutterIconBuilder<PsiElement> jumpToTestMethod = NavigationGutterIconBuilder.create(methodLineMarkerProvider.getIcon(element))
                    .setTooltipTitle("Jump to Test Method")
                    .setTargets(methodLineMarkerProvider.getAssociate(element));
            result.add(jumpToTestMethod.createLineMarkerInfo(element));
        }

    }
}
