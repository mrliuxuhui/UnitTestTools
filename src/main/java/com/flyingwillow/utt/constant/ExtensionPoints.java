package com.flyingwillow.utt.constant;

import com.flyingwillow.utt.extensionpoint.code.TestClassBuilder;
import com.flyingwillow.utt.extensionpoint.code.TestMethodBuilder;
import com.flyingwillow.utt.extensionpoint.dependence.DependenceBuilder;
import com.flyingwillow.utt.extensionpoint.dependence.DependenceManager;

import java.util.Arrays;
import java.util.List;

public class ExtensionPoints {

    public static final List<Class> extensionClasses = Arrays.asList(DependenceBuilder.class, DependenceManager.class,
            TestClassBuilder.class, TestMethodBuilder.class);

    public static final String NS = "com.flyingwillow.utt.";

    private ExtensionPoints() {
    }
}
