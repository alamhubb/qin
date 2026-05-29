package com.qin.runtime.spring;

import com.qin.runtime.core.QinSpringCompileUnit;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.server.context.WebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Shared Spring host glue for Qin-generated JVM classes.
 *
 * This keeps application shells small while moving registration logic toward a
 * reusable Spring integration boundary.
 */
public final class QinSpringHostSupport {
    private QinSpringHostSupport() {
    }

    public static ConfigurableApplicationContext run(
            Class<?> applicationClass,
            String[] args,
            Path... sourceFiles) throws Exception {
        Objects.requireNonNull(applicationClass, "applicationClass cannot be null");
        return run(new SpringApplication(applicationClass), applicationClass.getClassLoader(), args, sourceFiles);
    }

    public static ConfigurableApplicationContext run(
            SpringApplication application,
            ClassLoader parent,
            String[] args,
            Path... sourceFiles) throws Exception {
        Objects.requireNonNull(application, "application cannot be null");
        QinSpringCompiledSources compiled = compileSources(parent, sourceFiles);
        application.addInitializers(context -> registerSpringBeans(context, compiled.springBeanClasses()));
        return application.run(args != null ? args : new String[0]);
    }

    public static QinSpringCompiledSources compileSources(
            ClassLoader parent,
            Path... sourceFiles) throws Exception {
        Objects.requireNonNull(parent, "parent cannot be null");
        QinSpringCompileUnit compileUnit = QinSpringCompileUnit.compileAll(sourceFiles);
        Map<String, Class<?>> definedClasses = compileUnit.defineAll(parent);
        List<Class<?>> springBeans = new ArrayList<>();
        for (Class<?> definedClass : definedClasses.values()) {
            if (isSpringBeanClass(definedClass)) {
                springBeans.add(definedClass);
            }
        }

        return new QinSpringCompiledSources(compileUnit.compiledClasses(), definedClasses, springBeans);
    }

    public static void registerSpringBeans(
            ConfigurableApplicationContext context,
            Iterable<Class<?>> beanClasses) {
        Objects.requireNonNull(context, "context cannot be null");
        Objects.requireNonNull(beanClasses, "beanClasses cannot be null");

        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) context.getBeanFactory();
        for (Class<?> beanClass : beanClasses) {
            String beanName = beanClass.getName();
            if (!registry.containsBeanDefinition(beanName)) {
                registry.registerBeanDefinition(beanName, new RootBeanDefinition(beanClass));
            }
        }
    }

    public static int resolvePort(ConfigurableApplicationContext context) {
        Objects.requireNonNull(context, "context cannot be null");
        if (context instanceof WebServerApplicationContext webServerApplicationContext) {
            return webServerApplicationContext.getWebServer().getPort();
        }
        throw new IllegalStateException("Spring context is not a web server application context");
    }

    public static boolean isSpringBeanClass(Class<?> candidate) {
        Objects.requireNonNull(candidate, "candidate cannot be null");
        return AnnotatedElementUtils.hasAnnotation(candidate, Component.class);
    }

    public record QinSpringCompiledSources(
            Map<String, byte[]> compiledClasses,
            Map<String, Class<?>> definedClasses,
            List<Class<?>> springBeanClasses) {
        public QinSpringCompiledSources {
            compiledClasses = Map.copyOf(compiledClasses);
            definedClasses = Map.copyOf(definedClasses);
            springBeanClasses = List.copyOf(springBeanClasses);
        }
    }
}
