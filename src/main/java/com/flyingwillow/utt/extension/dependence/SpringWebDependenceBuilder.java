package com.flyingwillow.utt.extension.dependence;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsSchema;
import com.flyingwillow.utt.domain.Dependence;
import com.flyingwillow.utt.domain.ProjectInfo;
import com.flyingwillow.utt.extensionpoint.dependence.DependenceBuilder;
import com.intellij.openapi.diagnostic.Logger;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class SpringWebDependenceBuilder implements DependenceBuilder {

    private static final Logger LOG = Logger.getInstance(SpringWebDependenceBuilder.class);
    private static final String DEFAULT_CONFIG = "springWebDependencies.properties";
    private Properties properties;

    @Override
    public boolean match(ProjectInfo projectInfo) {
        return false;
    }

    @Override
    public List<Dependence> getDependenceList() {
        JavaPropsMapper mapper = new JavaPropsMapper();
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, Dependence.class);
        try {
            List<Dependence> dependencies = mapper.readerFor(type)
                    .with(JavaPropsSchema.emptySchema())
                    .withRootName("dependence")
                    .readValue(SpringWebDependenceBuilder.class.getClassLoader().getResourceAsStream(DEFAULT_CONFIG));
            return dependencies;
        } catch (IOException e) {
            LOG.error("read file exception!", e);
        }
        return Collections.emptyList();
    }
}
