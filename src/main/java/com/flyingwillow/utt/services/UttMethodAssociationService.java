package com.flyingwillow.utt.services;

import com.flyingwillow.utt.extensionpoint.cache.MethodAssociationCacheBuilder;
import com.flyingwillow.utt.extensionpoint.provider.UttMethodLineMarkerProvider;
import com.intellij.lang.Language;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public final class UttMethodAssociationService implements UttBaseService {

    private final ConcurrentHashMap<String, List<PsiElement>> cache = new ConcurrentHashMap<>();

    private final Map<Language, MethodAssociationCacheBuilder> associationCacheBuilderMap = new HashMap<>(10);

    /**
     * associate providers
     */
    private final Map<Language, UttMethodLineMarkerProvider> lineMarkerProviderMap = new HashMap<>(10);

    /**
     * get available associated test methods
     * if not hold in cache
     * then search in the corresponding test class
     */
    public List<PsiElement> getAssociations(PsiElement element) {

        final MethodAssociationCacheBuilder associationBuilder = associationCacheBuilderMap.get(element.getLanguage());

        List<PsiElement> associations = cache.get(associationBuilder.getUnifiedKey(element));
        if (CollectionUtils.isNotEmpty(associations)) {
            return associations;
        }

        associations = associationBuilder.getAssociate(element);

        if (CollectionUtils.isNotEmpty(associations)) {
            cache.put(associationBuilder.getUnifiedKey(element), new CopyOnWriteArrayList<>(associations));
        }

        return associations;
    }

    @Override
    public void init(Project project) {
        setupProviders();
        final List<MethodAssociationCacheBuilder> extensionList = MethodAssociationCacheBuilder.EXTENSION_POINT_NAME.getExtensionList();
        extensionList.forEach(methodAssociationCacheBuilder -> {
            associationCacheBuilderMap.put(methodAssociationCacheBuilder.getLanguage(), methodAssociationCacheBuilder);
            final Map<String, List<PsiElement>> psiElementMap = methodAssociationCacheBuilder.loadMap(project);
            if (MapUtils.isNotEmpty(psiElementMap)) {
                psiElementMap.entrySet().stream().filter(entry -> CollectionUtils.isNotEmpty(entry.getValue()))
                        .forEach(entry -> cache.put(entry.getKey(), new CopyOnWriteArrayList<>(entry.getValue())));
            }
        });
    }

    private void setupProviders() {
        List<UttMethodLineMarkerProvider> associates = UttMethodLineMarkerProvider.EXTENSION_POINT_NAME.getExtensionList();
        associates.forEach(associate -> lineMarkerProviderMap.put(associate.getLanguage(), associate));
        System.out.println("methodAssociate:" + lineMarkerProviderMap.keySet());
    }

    public UttMethodLineMarkerProvider getMethodAssociate(Language language) {
        return lineMarkerProviderMap.get(language);
    }

    /**
     * initialize cache
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
