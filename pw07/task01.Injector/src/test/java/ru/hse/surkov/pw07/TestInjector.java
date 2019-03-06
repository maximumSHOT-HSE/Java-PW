package ru.hse.surkov.pw07;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
                "ru.hse.surkov.pw07.ClassWithOneInterfaceDependency",
                Collections.singletonList("ru.hse.surkov.pw07.InterfaceImpl")
        );
        assertTrue(object instanceof ClassWithOneInterfaceDependency);
        ClassWithOneInterfaceDependency instance = (ClassWithOneInterfaceDependency) object;
        assertTrue(instance.dependency instanceof InterfaceImpl);
    }

    @Test
    void testCycleDependencies() {
        List<String> implementations = new ArrayList<>();
        implementations.add("ru.hse.surkov.pw07.A");
        implementations.add("ru.hse.surkov.pw07.B");
        implementations.add("ru.hse.surkov.pw07.C");
        assertThrows(InjectionCycleException.class, () -> Injector.initialize("ru.hse.surkov.pw07.A", implementations));
    }
}
