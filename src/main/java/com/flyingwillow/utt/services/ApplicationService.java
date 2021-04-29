package com.flyingwillow.utt.services;

import com.flyingwillow.utt.extensionpoint.ExtensionHolder;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.ExtensionPointName;

import java.util.HashMap;
import java.util.Map;

@Service
public final class ApplicationService {

    private final Logger logger = Logger.getInstance(ApplicationService.class);

    private final Map<Class, ExtensionHolder> extensionHolderMap = new HashMap<>(20);

    public void initializeExtensions(Map<Class, ExtensionPointName> extensionPointMap) {

        extensionPointMap.forEach((key, extensionPoint) -> extensionHolderMap.put(key, new ExtensionHolder(extensionPoint.getExtensionList())));
    }

    public <T> ExtensionHolder<T> getExtensions(Class<T> clazz){
        return extensionHolderMap.get(clazz);
    }
}
