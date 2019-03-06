package ru.hse.surkov.pw07;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestInjector {

    @Test
    public void injectorShouldInitializeClassWithoutDependencies()
            throws Exception, InjectionCycleException {
        Object object = Injector.initialize("ru.hse.surkov.pw07.ClassWithoutDependencies", Collections.emptyList());
        assertTrue(object instanceof ClassWithoutDependencies);
    }

    @Test
    public void injectorShouldInitializeClassWithOneClassDependency()
            throws Exception, InjectionCycleException {
        Object object = Injector.initialize(
                "ru.hse.surkov.pw07.ClassWithOneClassDependency",
                Collections.singletonList("ru.hse.surkov.pw07.ClassWithoutDependencies")
        );
        assertTrue(object instanceof ClassWithOneClassDependency);
        ClassWithOneClassDependency instance = (ClassWithOneClassDependency) object;
        assertTrue(instance.dependency != null);
    }

    @Test
    public void injectorShouldInitializeClassWithOneInterfaceDependency()
            throws Exception, InjectionCycleException {
        Object object = Injector.initialize(
                "task.testClasses.ClassWithOneInterfaceDependency",
                Collections.singletonList("task.testClasses.InterfaceImpl")
        );
        assertTrue(object instanceof ClassWithOneInterfaceDependency);
        ClassWithOneInterfaceDependency instance = (ClassWithOneInterfaceDependency) object;
        assertTrue(instance.dependency instanceof InterfaceImpl);
    }
}
