package com.qin.orm;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.FilerException;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SupportedAnnotationTypes("jakarta.persistence.Entity")
@SupportedSourceVersion(SourceVersion.RELEASE_25)
public final class QinOrmEntityProcessor extends AbstractProcessor {
    private static final Set<String> EXCLUDED_ANNOTATIONS = Set.of(
            "jakarta.persistence.Transient",
            "jakarta.persistence.OneToOne",
            "jakarta.persistence.OneToMany",
            "jakarta.persistence.ManyToOne",
            "jakarta.persistence.ManyToMany",
            "jakarta.persistence.Embedded",
            "jakarta.persistence.EmbeddedId",
            "jakarta.persistence.ElementCollection");

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        for (Element element : roundEnvironment.getElementsAnnotatedWith(
                processingEnv.getElementUtils().getTypeElement("jakarta.persistence.Entity"))) {
            if (element instanceof TypeElement entityType) {
                generateDescriptor(entityType);
            }
        }
        return false;
    }

    private void generateDescriptor(TypeElement entityType) {
        List<Property> properties = properties(entityType);
        PackageElement packageElement = processingEnv.getElementUtils().getPackageOf(entityType);
        String packageName = packageElement.getQualifiedName().toString();
        String entityName = entityType.getSimpleName().toString();
        String descriptorName = entityName + "Qin";
        String qualifiedDescriptorName = packageName.isEmpty()
                ? descriptorName
                : packageName + "." + descriptorName;
        try {
            JavaFileObject sourceFile = processingEnv.getFiler()
                    .createSourceFile(qualifiedDescriptorName, entityType);
            try (Writer writer = sourceFile.openWriter()) {
                writeDescriptor(writer, packageName, descriptorName, entityType, properties);
            }
        } catch (FilerException duplicate) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "Qin descriptor already exists: " + qualifiedDescriptorName, entityType);
        } catch (IOException error) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "Unable to generate Qin descriptor: " + error.getMessage(), entityType);
        }
    }

    private List<Property> properties(TypeElement entityType) {
        AccessKind accessKind = accessKind(entityType);
        List<TypeElement> hierarchy = hierarchy(entityType);
        Map<String, Property> properties = new LinkedHashMap<>();
        for (TypeElement owner : hierarchy) {
            if (accessKind == AccessKind.FIELD) {
                for (Element member : owner.getEnclosedElements()) {
                    if (member.getKind() != ElementKind.FIELD || excluded(member)) {
                        continue;
                    }
                    var field = (javax.lang.model.element.VariableElement) member;
                    if (field.getModifiers().contains(Modifier.STATIC)
                            || field.getModifiers().contains(Modifier.TRANSIENT)
                            || !supportedPropertyType(field.asType())) {
                        continue;
                    }
                    properties.put(field.getSimpleName().toString(), new Property(
                            field.getSimpleName().toString(), field.asType()));
                }
            } else {
                for (ExecutableElement method : ElementFilter.methodsIn(owner.getEnclosedElements())) {
                    if (method.getModifiers().contains(Modifier.STATIC)
                            || !method.getParameters().isEmpty()
                            || method.getReturnType().getKind() == TypeKind.VOID
                            || excluded(method)) {
                        continue;
                    }
                    String propertyName = propertyName(method);
                    if (propertyName == null || !supportedPropertyType(method.getReturnType())) {
                        continue;
                    }
                    properties.put(propertyName, new Property(propertyName, method.getReturnType()));
                }
            }
        }
        return List.copyOf(properties.values());
    }

    private AccessKind accessKind(TypeElement entityType) {
        String declaredAccess = annotationEnumValue(entityType, "jakarta.persistence.Access", "value");
        if (declaredAccess != null) {
            return declaredAccess.endsWith("PROPERTY") ? AccessKind.PROPERTY : AccessKind.FIELD;
        }
        for (TypeElement owner : hierarchy(entityType)) {
            for (Element member : owner.getEnclosedElements()) {
                if (hasAnnotation(member, "jakarta.persistence.Id")
                        || hasAnnotation(member, "jakarta.persistence.EmbeddedId")) {
                    return member.getKind() == ElementKind.METHOD ? AccessKind.PROPERTY : AccessKind.FIELD;
                }
            }
        }
        return AccessKind.FIELD;
    }

    private List<TypeElement> hierarchy(TypeElement entityType) {
        List<TypeElement> result = new ArrayList<>();
        TypeElement current = entityType;
        while (current != null && !current.getQualifiedName().contentEquals("java.lang.Object")) {
            if (current.equals(entityType) || hasAnnotation(current, "jakarta.persistence.Entity")
                    || hasAnnotation(current, "jakarta.persistence.MappedSuperclass")) {
                result.add(current);
            }
            TypeMirror superclass = current.getSuperclass();
            if (superclass.getKind() != TypeKind.DECLARED) {
                break;
            }
            Element parent = ((DeclaredType) superclass).asElement();
            current = parent instanceof TypeElement typeElement ? typeElement : null;
        }
        Collections.reverse(result);
        return result;
    }

    private boolean supportedPropertyType(TypeMirror type) {
        if (type.getKind().isPrimitive() || type.getKind() == TypeKind.ARRAY) {
            return true;
        }
        if (type.getKind() != TypeKind.DECLARED) {
            return false;
        }
        DeclaredType declaredType = (DeclaredType) type;
        if (!declaredType.getTypeArguments().isEmpty()) {
            return false;
        }
        Element propertyType = declaredType.asElement();
        if (propertyType instanceof TypeElement typeElement) {
            if (hasAnnotation(typeElement, "jakarta.persistence.Entity")
                    || hasAnnotation(typeElement, "jakarta.persistence.Embeddable")) {
                return false;
            }
        }
        TypeElement collectionType = processingEnv.getElementUtils().getTypeElement("java.util.Collection");
        TypeElement mapType = processingEnv.getElementUtils().getTypeElement("java.util.Map");
        TypeMirror erasedType = processingEnv.getTypeUtils().erasure(type);
        return (collectionType == null || !processingEnv.getTypeUtils().isAssignable(
                        erasedType, processingEnv.getTypeUtils().erasure(collectionType.asType())))
                && (mapType == null || !processingEnv.getTypeUtils().isAssignable(
                        erasedType, processingEnv.getTypeUtils().erasure(mapType.asType())));
    }

    private boolean excluded(Element element) {
        for (String annotationName : EXCLUDED_ANNOTATIONS) {
            if (hasAnnotation(element, annotationName)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasAnnotation(Element element, String annotationName) {
        return element.getAnnotationMirrors().stream()
                .anyMatch(annotation -> annotation.getAnnotationType().toString().equals(annotationName));
    }

    private String annotationEnumValue(Element element, String annotationName, String memberName) {
        return element.getAnnotationMirrors().stream()
                .filter(annotation -> annotation.getAnnotationType().toString().equals(annotationName))
                .flatMap(annotation -> annotation.getElementValues().entrySet().stream())
                .filter(entry -> entry.getKey().getSimpleName().contentEquals(memberName))
                .map(entry -> entry.getValue().getValue().toString())
                .findFirst()
                .orElse(null);
    }

    private String propertyName(ExecutableElement method) {
        String methodName = method.getSimpleName().toString();
        String property;
        if (methodName.startsWith("get") && methodName.length() > 3) {
            property = methodName.substring(3);
        } else if (methodName.startsWith("is") && methodName.length() > 2
                && method.getReturnType().getKind() == TypeKind.BOOLEAN) {
            property = methodName.substring(2);
        } else {
            return null;
        }
        if (property.length() > 1 && Character.isUpperCase(property.charAt(0))
                && Character.isUpperCase(property.charAt(1))) {
            return property;
        }
        return Character.toLowerCase(property.charAt(0)) + property.substring(1);
    }

    private void writeDescriptor(
            Writer writer,
            String packageName,
            String descriptorName,
            TypeElement entityType,
            List<Property> properties) throws IOException {
        if (!packageName.isEmpty()) {
            writer.write("package " + packageName + ";\n\n");
        }
        writer.write("@javax.annotation.processing.Generated(\"com.qin.orm.QinOrmEntityProcessor\")\n");
        writer.write("public final class " + descriptorName + " extends com.qin.orm.QinOrmEntity<"
                + entityType.getQualifiedName() + "> {\n");
        for (Property property : properties) {
            String boxedType = boxedType(property.type());
            writer.write("    public final com.qin.orm.QinOrmColumn<" + entityType.getQualifiedName() + ", "
                    + boxedType + "> " + property.name() + ";\n");
        }
        writer.write("\n    public " + descriptorName + "() {\n");
        writer.write("        super(" + entityType.getQualifiedName() + ".class);\n");
        for (Property property : properties) {
            writer.write("        this." + property.name() + " = column(\"" + property.name() + "\", "
                    + processingEnv.getTypeUtils().erasure(property.type()) + ".class);\n");
        }
        writer.write("    }\n}\n");
    }

    private String boxedType(TypeMirror type) {
        if (type.getKind().isPrimitive()) {
            return processingEnv.getTypeUtils().boxedClass((javax.lang.model.type.PrimitiveType) type)
                    .getQualifiedName().toString();
        }
        return type.toString();
    }

    private enum AccessKind {
        FIELD,
        PROPERTY
    }

    private record Property(String name, TypeMirror type) {
    }
}
