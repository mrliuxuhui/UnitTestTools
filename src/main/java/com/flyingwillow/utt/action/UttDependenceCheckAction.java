package com.flyingwillow.utt.action;

import com.flyingwillow.utt.services.ProjectInfoService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import org.jetbrains.annotations.NotNull;

public class UttDependenceCheckAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {

        // check dependencies
        ProjectInfoService projectInfoService = ServiceManager.getService(ProjectInfoService.class);
        projectInfoService.getMissedDependencies(anActionEvent.getProject());
    }
}
