package com.flyingwillow.utt.listeners;

import com.flyingwillow.utt.constant.CoreServices;
import com.flyingwillow.utt.services.UttBaseService;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectUttInitializer implements ProjectManagerListener {

    private Logger logger = Logger.getInstance(ProjectUttInitializer.class);

    @Override
    public void projectOpened(@NotNull Project project) {

        logger.info("project opened");
        System.out.println("project opened");

        // initializing
        initializeServices(project);

        // publish event -> to reading saved utt data, rendering icons on method

        logger.info("project initialization completed!");
        System.out.println("project initialization completed!");
    }

    private void initializeServices(final Project project) {
        final List<UttBaseService> services = Arrays.stream(CoreServices.CORE_SERVICES).map(cls -> ((UttBaseService) ServiceManager.getService(cls)))
                .sorted(Comparator.comparingInt(UttBaseService::getOrder))
                .collect(Collectors.toList());
        services.forEach(s -> s.init(project));
    }

    @Override
    public void projectClosing(@NotNull Project project) {

        logger.info("project closing");
        // saving utt data
    }
}
