package com.flyingwillow.utt.services;

import com.flyingwillow.utt.extensionpoint.dependence.DependenceBuilder;
import com.flyingwillow.utt.extensionpoint.dependence.DependenceManager;
import com.flyingwillow.utt.domain.ProjectInfo;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;

@Service
public final class ProjectInfoService {

    private final Logger logger = Logger.getInstance(ProjectInfoService.class);

    /**
     *  project info
     * */
    private ProjectInfo projectInfo;

    /**
     * project default Dependence Manager
     * */
    private DependenceManager dependenceManager;

    /**
     * project default Dependence builder
     * */
    private DependenceBuilder dependenceBuilder;

    public ProjectInfo getProjectInfo(){
        return projectInfo;
    }

    /**
     *  initialize project info
     * */
    private void collectProjectInfo(Project project) {
        logger.info("collecting project info...");
        System.out.println("collecting project info...");
        System.out.println("project = " + project);
    }

    private void checkDependencies() {
        logger.info("checking dependencies...");
        System.out.println("checking dependencies...");
    }

    /**
     *  initializing
     * */
    public void initialize(Project project) {
        // collect project info
        collectProjectInfo(project);

        // setup dependence manager/ code manager
        setupManagers();

        // check dependencies
        checkDependencies();


    }

    private void setupManagers() {
        System.out.println("setup managers...");
    }
}
