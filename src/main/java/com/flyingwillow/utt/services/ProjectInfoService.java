package com.flyingwillow.utt.services;

import com.flyingwillow.utt.domain.MissMatchDependence;
import com.flyingwillow.utt.domain.ProjectInfo;
import com.flyingwillow.utt.extensionpoint.dependence.DependenceBuilder;
import com.flyingwillow.utt.extensionpoint.dependence.DependenceManager;
import com.flyingwillow.utt.extensionpoint.provider.UttMethodAssociate;
import com.intellij.lang.Language;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public final class ProjectInfoService {

    private final Logger logger = Logger.getInstance(ProjectInfoService.class);

    private Project project;

    /**
     * project info
     */
    private ProjectInfo projectInfo;

    /**
     * project default Dependence Manager
     */
    private DependenceManager dependenceManager;

    /**
     * project default Dependence builder
     */
    private DependenceBuilder dependenceBuilder;

    /**
     * associate providers
     */
    private final Map<Language, UttMethodAssociate> methodAssociateHashMap = new HashMap<>(10);

    public Project getProject() {
        return this.project;
    }

    public ProjectInfo getProjectInfo() {
        return projectInfo;
    }

    /**
     * initialize project info
     */
    private void collectProjectInfo(Project project) {
        logger.info("collecting project info...");
        System.out.println("collecting project info...");
        System.out.println("project = " + project);
    }

    public List<MissMatchDependence> getMissedDependencies(Project project) {
        return dependenceManager.checkDependencies(dependenceBuilder.getDependenceList(), project);
    }

    public void checkDependencies(Project project) {
        logger.info("checking dependencies...");
        System.out.println("checking dependencies...");

        System.out.println(this.dependenceBuilder.getDependenceList());

        List<MissMatchDependence> missMatchDependencies = getMissedDependencies(project);

        if (!CollectionUtils.isEmpty(missMatchDependencies)) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n");
            missMatchDependencies.forEach(missMatchDependence -> sb.append(missMatchDependence.getStringMsg()).append("\n"));
            System.out.println("mismatch = " + sb.toString());

            Notification notification = NotificationGroupManager.getInstance().getNotificationGroup("Utt Notification Group")
                    .createNotification("Missing Dependencies", sb.toString(), NotificationType.WARNING, (notification1, event) -> {

                    });
            notification.addAction(ActionManager.getInstance().getAction("uttAction.fixupDependence"));

            Notifications.Bus.notify(notification, project);
        }

    }

    /**
     * initializing
     */
    public void initialize(Project project) {
        this.project = project;
        // collect project info
        collectProjectInfo(project);

        // setup dependence manager/ code manager
        setupManagers();

        // set providers
        setupProviders();

        // check dependencies
        checkDependencies(project);

    }

    private void setupProviders() {
        List<UttMethodAssociate> associates = UttMethodAssociate.EXTENSION_POINT_NAME.getExtensionList();
        associates.forEach(associate -> methodAssociateHashMap.put(associate.getLanguage(), associate));
        System.out.println("methodAssociate:" + methodAssociateHashMap.keySet());
    }

    private void setupManagers() {
        System.out.println("setup managers...");
        List<DependenceBuilder> dependenceBuilders = DependenceBuilder.EXTENSION_POINT_NAME.getExtensionList();
        this.dependenceBuilder = dependenceBuilders.get(0);

        List<DependenceManager> dependenceManagers = DependenceManager.EXTENSION_POINT_NAME.getExtensionList();
        this.dependenceManager = dependenceManagers.get(0);

    }

    public void setupDependencies(Project project) {
        this.dependenceManager.setupIfNecessary(dependenceBuilder.getDependenceList(), project);
    }

    public UttMethodAssociate getMethodAssociate(Language language) {
        return methodAssociateHashMap.get(language);
    }
}
