package com.flyingwillow.utt.extension.dependence;

import com.flyingwillow.utt.domain.Dependence;
import com.flyingwillow.utt.domain.MissMatchDependence;
import com.flyingwillow.utt.domain.ProjectInfo;
import com.flyingwillow.utt.extensionpoint.dependence.DependenceManager;
import com.flyingwillow.utt.services.ProjectInfoService;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiManager;
import com.intellij.psi.XmlElementFactory;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ArrayUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MavenDependenceManager implements DependenceManager {

    private static final String DEPENDENCE_PATTERN = "<dependency>" +
            "<groupId>{groupId}</groupId>" +
            "<artifactId>{artifactId}</artifactId>" +
            "<version>{version}</version>" +
            "<scope>{scope}</scope>" +
            "</dependency>";

    private static final String TAG_PATTERN = "<{tag}>{value}</{tag}>";
    public static final String VERSION = "version";
    public static final String SCOPE = "scope";

    @Override
    public boolean match(ProjectInfo projectInfo) {
        return false;
    }

    @Override
    public void setupIfNecessary(List<Dependence> list, Project project) {
        XmlFile pom = getPomFile(project);
        XmlElementFactory factory = getXmlElementFactory();
        XmlTag dependencies = pom.getDocument().getRootTag().findFirstSubTag("dependencies");
        if (null == dependencies ) {
            XmlTag dependenciesTag = factory.createTagFromText("<dependencies></dependencies>");
            list.forEach(d -> dependenciesTag.addSubTag(getTag(d, factory), false));
            System.out.println("dependencies = " + dependenciesTag.toString());
            WriteCommandAction.writeCommandAction(project, pom).run(() -> pom.getRootTag().addSubTag(dependenciesTag, false));
        } else {
            Map<String, XmlTag> dependenceMap = getMap(dependencies);
            list.forEach(d -> {
                String sb = getTagString(d.getGroupId(), d.getArtifactId());
                if (!dependenceMap.containsKey(sb)) {
                    System.out.println("list = " + sb);
                    XmlTag tag = getTag(d, factory);
                    System.out.println("tag = " + tag);
                    WriteCommandAction.writeCommandAction(project, pom).run(() -> dependencies.addSubTag(tag, false));
                } else {
                    XmlTag current = dependenceMap.get(sb);
                    fixupDependence(project, pom, factory, current, VERSION, d.getVersion());
                    fixupDependence(project, pom, factory, current, SCOPE, d.getScope());
                }
            });
        }
    }

    @NotNull
    private void fixupDependence(Project project, XmlFile pom, XmlElementFactory factory, XmlTag current, String tag, String value) {
        XmlTag valueTag = current.findFirstSubTag(tag);
        String tagString = TAG_PATTERN.replace("{tag}", tag).replace("{value}", value);
        if (valueTag == null) {
            WriteCommandAction.writeCommandAction(project, pom).run(() -> current.addSubTag(factory.createTagFromText(tagString), false));
        } else if (!value.equals(valueTag.getValue().getText())) {
            WriteCommandAction.writeCommandAction(project, pom).run(() -> valueTag.replace(factory.createTagFromText(tagString)));
        }
    }

    @Override
    public List<MissMatchDependence> checkDependencies(List<Dependence> list, Project project) {
        XmlFile pom = getPomFile(project);
        XmlTag dependencies = pom.getDocument().getRootTag().findFirstSubTag("dependencies");
        if (dependencies == null || ArrayUtil.isEmpty(dependencies.findSubTags("dependency"))) {
            return list.stream().map(dependence -> {
                MissMatchDependence missMatchDependence = new MissMatchDependence();
                missMatchDependence.setMissed(true);
                missMatchDependence.setTarget(dependence);
                return missMatchDependence;
            }).collect(Collectors.toList());
        } else {
            Map<String, XmlTag> dependenceMap = getMap(dependencies);
            return list.stream().filter(dependence -> !allMatch(dependence, dependenceMap.get(getTagString(dependence.getGroupId(), dependence.getArtifactId()))))
                    .map(dependence -> {
                        XmlTag tag = dependenceMap.get(getTagString(dependence.getGroupId(), dependence.getArtifactId()));
                        MissMatchDependence missMatchDependence = new MissMatchDependence();
                        missMatchDependence.setTarget(dependence);
                        String scope = tag.getSubTagText(SCOPE);
                        String version = tag.getSubTagText(VERSION);
                        missMatchDependence.setScope(scope);
                        missMatchDependence.setScopeMismatch(!dependence.getScope().equals(scope));
                        missMatchDependence.setVersion(version);
                        missMatchDependence.setVersionMismatch(!dependence.getVersion().equals(version));
                        return missMatchDependence;
                    }).collect(Collectors.toList());
        }
    }

    private boolean allMatch(Dependence dependence, XmlTag tag) {
        return dependence.getVersion().equals(tag.getSubTagText(VERSION)) &&
                dependence.getScope().equals(tag.getSubTagText(SCOPE));
    }

    private XmlElementFactory getXmlElementFactory() {
        ProjectInfoService service = ServiceManager.getService(ProjectInfoService.class);
        return XmlElementFactory.getInstance(service.getProject());
    }

    private XmlTag getTag(Dependence d, XmlElementFactory factory) {
        String tagString = DEPENDENCE_PATTERN.replace("{groupId}", d.getGroupId())
                .replace("{artifactId}", d.getArtifactId())
                .replace("{version}", d.getVersion())
                .replace("{scope}", d.getScope());
        return factory.createTagFromText(tagString);
    }

    private Map<String, XmlTag> getMap(XmlTag dependencies) {

        Map<String, XmlTag> map = new HashMap<>();
        for (XmlTag tag : dependencies.getSubTags()) {
            String sb = getTagString(tag.getSubTagText("groupId"),
                    tag.getSubTagText("artifactId"));
            map.put(sb, tag);
        }
        return map;
    }

    private String getTagString(String groupId, String artifactId) {
        StringBuilder sb = new StringBuilder();
        sb.append(groupId).append("-")
                .append(artifactId);
        return sb.toString();
    }

    private XmlFile getPomFile(Project project) {
        File pomFile = new File(project.getBasePath() + "/pom.xml");
        VirtualFile v = VirtualFileManager.getInstance().findFileByNioPath(pomFile.toPath());
        return (XmlFile) PsiManager.getInstance(project).findFile(v).getViewProvider().getPsi(XMLLanguage.INSTANCE);
    }
}
