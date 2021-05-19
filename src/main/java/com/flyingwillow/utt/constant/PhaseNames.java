package com.flyingwillow.utt.constant;

import com.intellij.util.containers.ContainerUtil;

import java.util.Map;
import java.util.regex.Pattern;

public class PhaseNames {
    private PhaseNames() {
    }

    public static final String GIVEN = "given";
    public static final String WHEN = "when";
    public static final String THEN = "then";

    public static final Pattern GIVEN_PATTERN = Pattern.compile("^//.+(?i)given$");
    public static final Pattern WHEN_PATTERN = Pattern.compile("^//.+(?i)when$");
    public static final Pattern THEN_PATTERN = Pattern.compile("^//.+(?i)then$");

    public static final Map<String, Pattern> patternMap = ContainerUtil.<String, Pattern>immutableMapBuilder().put(GIVEN, GIVEN_PATTERN)
            .put(WHEN, WHEN_PATTERN).put(THEN,THEN_PATTERN).build();

}
