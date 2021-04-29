package com.flyingwillow.utt.listeners;

import com.flyingwillow.utt.constant.ExtensionPoints;
import com.flyingwillow.utt.services.ApplicationService;
import com.intellij.ide.plugins.DynamicPluginListener;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.ExtensionPointName;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.flyingwillow.utt.constant.ExtensionPoints.NS;

public class ApplicationInitializer implements DynamicPluginListener {

    private final Logger logger = Logger.getInstance(ApplicationInitializer.class);

    private final Map<Class, ExtensionPointName> extensionPointMap = new HashMap<>();


    private void initExtensionPoints() {
        ExtensionPoints.extensionClasses.forEach(clazz -> extensionPointMap.put(clazz, ExtensionPointName.create(NS + clazz.getSimpleName())));
    }

    @Override
    public void pluginLoaded(@NotNull IdeaPluginDescriptor pluginDescriptor) {
        logger.warn("application started");
        System.out.println("application started");
        initExtensionPoints();
        logger.info("extension points initialized!");
        System.out.println("extension points initialized!");
        ApplicationService applicationService = ServiceManager.getService(ApplicationService.class);
        applicationService.initializeExtensions(extensionPointMap);
    }
}
