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
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiTypesUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaMethodAssociationBuilder implements MethodAssociationBuilder {
    @Override
    public String getUnifiedKey(PsiElement element) {
        if(element instanceof PsiIdentifier && element.getParent() instanceof PsiMethod){
            final PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
            StringBuilder sb = new StringBuilder();
            final PsiMethod method = (PsiMethod) element.getParent();
            sb.append(psiClass.getQualifiedName())
                    .append(".")
                    .append(method.getName())
                    .append(method.getParameterList().getText());

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
        if(!(element instanceof PsiIdentifier) || !(element.getParent() instanceof PsiMethod)){
            return Collections.emptyList();
        }
        final PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
        final PsiMethod method = (PsiMethod) element.getParent();
        if(isTestClass(method)){
            return getTargetMethod(method, psiClass);
        } else {
            return getValidTestMethods(method, psiClass);
        }

    }

    private List<PsiElement> getTargetMethod(PsiMethod testMethod, PsiClass testClass) {

        final String testClassQualifiedName = testClass.getQualifiedName();
        final Project project = testMethod.getProject();
        Stream<PsiField> fields = Arrays.stream(testClass.getFields()).filter(psiField -> psiField.hasAnnotation("org.mockito.InjectMocks") ||
                psiField.hasAnnotation("org.springframework.beans.factory.annotation.Autowired"));

        if(testClassQualifiedName.substring(testClassQualifiedName.length() - 4).equals("Test")){
            PsiClass targetClass = JavaPsiFacade.getInstance(project).findClass(testClassQualifiedName.substring(0, testClassQualifiedName.length()-4), GlobalSearchScope.projectScope(project));
            if(null!=targetClass){
                fields = fields.filter(psiField -> PsiTypesUtil.compareTypes(psiField.getType(), PsiTypesUtil.getClassType(targetClass), false));
            }
        }

        final Optional<PsiField> first = fields.findFirst();
        if(!first.isPresent()){
            return Collections.emptyList();
        }

        final Optional<PsiMethodCallExpression> methodCallExpression = PsiTreeUtil.findChildrenOfType(testMethod, PsiMethodCallExpression.class).stream()
                .filter(mce -> mce.getMethodExpression().getFirstChild().getReference().isReferenceTo(first.get()))
                .findFirst();
        if(methodCallExpression.isPresent()){
            return Collections.singletonList(methodCallExpression.get().resolveMethod());
        }else {
            return Collections.emptyList();
        }
    }

    private boolean isTestClass(PsiMethod method) {
        return method.hasAnnotation("org.junit.Test");
    }

    private List<PsiElement> getValidTestMethods(PsiMethod psiMethod, PsiClass psiClass) {
        final String testClassName = psiClass.getQualifiedName() + "Test";
        System.out.println("test class = " + testClassName);
        final Project project = psiMethod.getProject();
        final PsiClass testClass = JavaPsiFacade.getInstance(project).findClass(testClassName, GlobalSearchScope.projectScope(project));
        // not a valid test class
        if(null == testClass || !hasInjectField(psiClass, testClass)){
            return Collections.emptyList();
        }
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
