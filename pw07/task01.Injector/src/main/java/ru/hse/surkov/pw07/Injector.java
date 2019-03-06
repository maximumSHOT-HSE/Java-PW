package ru.hse.surkov.pw07;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.*;


public class Injector {

    static Set<String> classesInProgress = new TreeSet<>();

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */
    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws
            Exception,
            InjectionCycleException {

        if (classesInProgress.contains(rootClassName)) {
            throw new InjectionCycleException();
        }
        classesInProgress.add(rootClassName);

        Class<?> rootClass = Class.forName(rootClassName);
        Constructor constructor = rootClass.getConstructors()[0];
        Parameter[] parameters = constructor.getParameters();
        List<Class<?>> implementationClasses = new ArrayList<>();
        for (String implementationClassName : implementationClassNames) {
            implementationClasses.add(Class.forName(implementationClassName));
        }
        List<Object> parametrInstances = new ArrayList<>();
        for (var parameter : parameters) {
            String needClassName = parameter.getName();
            Class<?> lastImplementationClass = null;
            for (var implementationClass : implementationClasses) {
                if (parameter.getClass().isAssignableFrom(implementationClass)) {
                    if (lastImplementationClass != null) {
                        throw new AmbiguousImplementationException();
                    }
                    lastImplementationClass = parameter.getClass();
                }
            }
            if (lastImplementationClass == null) {
                throw new ImplementationNotFoundException();
            }
            parametrInstances.add(Injector.initialize(lastImplementationClass.getName(), implementationClassNames));
        }
        classesInProgress.remove(rootClassName);
        return constructor.newInstance(parametrInstances.toArray());
    }
}
