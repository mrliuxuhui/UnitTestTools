package com.flyingwillow.utt.extension.cache;

import com.flyingwillow.utt.extensionpoint.cache.MethodAssociationBuilder;
import com.flyingwillow.utt.services.UttCodeManageService;
import com.intellij.lang.Language;
import com.intellij.lang.StdLanguages;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiTypesUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JavaMethodAssociationBuilder implements MethodAssociationBuilder {
    @Override
    public String getUnifiedKey(PsiElement element) {
        if(element instanceof PsiIdentifier && element.getParent() instanceof PsiMethod){
            final PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
            StringBuilder sb = new StringBuilder();
            sb.append(psiClass.getQualifiedName())
                    .append(".")
                    .append(((PsiMethod)element.getParent()).getName());

            return sb.toString();
        }
        return "";
    }

    @Override
    public Map<String, List<PsiElement>> loadMap(Project project) {
        return Collections.emptyMap();
    }

    @Override
    public List<PsiElement> getAssociate(PsiElement element) {
        final PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
        final String testClassName = psiClass.getQualifiedName() + "Test";
        System.out.println("test class = " + testClassName);
        final PsiClass testClass = JavaPsiFacade.getInstance(element.getProject()).findClass(testClassName, GlobalSearchScope.projectScope(element.getProject()));
        // not a valid test class
        if(null == testClass || !hasInjectField(psiClass, testClass)){
            return Collections.emptyList();
        }

        return getValidTestMethods(PsiTreeUtil.getParentOfType(element, PsiMethod.class), testClass);
    }

    private List<PsiElement> getValidTestMethods(PsiMethod psiMethod, PsiClass testClass) {
        return Arrays.stream(testClass.getMethods()).filter(method -> method.hasAnnotation("org.junit.Test"))
                .filter(method -> haveCalledTargetMethod(method, psiMethod)).collect(Collectors.toList());
    }

    private boolean haveCalledTargetMethod(PsiMethod testMethod, PsiMethod targetMethod) {
        UttCodeManageService service = ServiceManager.getService(UttCodeManageService.class);
        return service.validateWhenPhase(targetMethod, testMethod);
    }

    private boolean hasInjectField(PsiClass psiClass, PsiClass testClass) {
        return Arrays.stream(testClass.getFields()).filter(psiField ->
                (psiField.hasAnnotation("org.mockito.InjectMocks") ||
                        psiField.hasAnnotation("org.springframework.beans.factory.annotation.Autowired")) &&
                PsiTypesUtil.compareTypes(psiField.getType(), PsiTypesUtil.getClassType(psiClass), false)).count() > 0;
    }

    @Override
    public Language getLanguage() {
        return StdLanguages.JAVA;
    }
}
