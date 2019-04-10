package ru.hse.surkov.pw07;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.*;


public class Injector {

    static Set<String> classesInProgress = new TreeSet<>();

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     * @throws InjectionCycleException if there is dependency cycle
     * @throws ClassNotFoundException if class with rootClassName has not been found
     * @throws AmbiguousImplementationException if there is more then one implementation of at least one class in dependencies
     * @throws ImplementationNotFoundException if there is at least one dependency class without implementation
     */
    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws
            Exception,
            InjectionCycleException {

        if (classesInProgress.contains(rootClassName)) {
            throw new InjectionCycleException();
        }
        classesInProgress.add(rootClassName);

        Class<?> rootClass = Class.forName(rootClassName);
        Constructor constructor = rootClass.getConstructors()[0]; // it is guaranteed that there is exactly one constructor
        Parameter[] parameters = constructor.getParameters();
        List<Class<?>> implementationClasses = new ArrayList<>();
        for (String implementationClassName : implementationClassNames) {
            implementationClasses.add(Class.forName(implementationClassName));
        }
        List<Object> parametrInstances = new ArrayList<>();
        for (var parameter : parameters) {
            Class<?> lastImplementationClass = null;
            for (var implementationClass : implementationClasses) {
                if (parameter.getType().isAssignableFrom(implementationClass)) {
                    if (lastImplementationClass != null) {
                        throw new AmbiguousImplementationException();
                    }
                    lastImplementationClass = implementationClass;
                }
            }
            if (lastImplementationClass == null) {
                throw new ImplementationNotFoundException();
            }
            parametrInstances.add(Injector.initialize(lastImplementationClass.getCanonicalName(), implementationClassNames));
        }
        classesInProgress.remove(rootClassName);
        return constructor.newInstance(parametrInstances.toArray());
    }
}
