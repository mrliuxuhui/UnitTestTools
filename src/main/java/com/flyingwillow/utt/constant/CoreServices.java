package com.flyingwillow.utt.constant;

import com.flyingwillow.utt.services.ProjectInfoService;
import com.flyingwillow.utt.services.UttCodeBuildService;
import com.flyingwillow.utt.services.UttCodeManageService;
import com.flyingwillow.utt.services.UttMethodAssociationService;

public class CoreServices {
    private CoreServices() {
    }

    public static final Class[] CORE_SERVICES = {ProjectInfoService.class, UttCodeManageService.class, UttMethodAssociationService.class, UttCodeBuildService.class};
}
