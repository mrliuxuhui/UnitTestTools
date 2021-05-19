package com.flyingwillow.utt.services;

import com.flyingwillow.utt.extensionpoint.cache.MethodAssociationBuilder;
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
public final class UttMethodAssociationService {

    private final ConcurrentHashMap<String, List<PsiElement>> cache = new ConcurrentHashMap<>();

    private final Map<Language, MethodAssociationBuilder> builderMap = new HashMap<>(10);

    /**
     *  initialize cache
     * */
    public void initialize(Project project){

        //
        final List<MethodAssociationBuilder> extensionList = MethodAssociationBuilder.EXTENSION_POINT_NAME.getExtensionList();
        extensionList.forEach(methodAssociationBuilder -> {
            builderMap.put(methodAssociationBuilder.getLanguage(), methodAssociationBuilder);
            final Map<String, List<PsiElement>> psiElementMap = methodAssociationBuilder.loadMap(project);
            if(MapUtils.isNotEmpty(psiElementMap)){
                psiElementMap.entrySet().stream().filter(entry -> CollectionUtils.isNotEmpty(entry.getValue()))
                        .forEach(entry -> cache.put(entry.getKey(), new CopyOnWriteArrayList<>(entry.getValue())));
            }
        });

    }

    /**
     *  get available associated test methods
     *  if not hold in cache
     *  then search in the corresponding test class
     * */
    public List<PsiElement> getAssociations(PsiElement element){

        final MethodAssociationBuilder associationBuilder = builderMap.get(element.getLanguage());

        List<PsiElement> associations = cache.get(associationBuilder.getUnifiedKey(element));
        if(CollectionUtils.isNotEmpty(associations)){
            return associations;
        }

        associations = associationBuilder.getAssociate(element);

        if(CollectionUtils.isNotEmpty(associations)){
            cache.put(associationBuilder.getUnifiedKey(element), new CopyOnWriteArrayList<>(associations));
        }

        return associations;
    }
}
