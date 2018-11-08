package com.chaos;

import java.lang.reflect.Modifier;

/**
 * Created by chaos on 2018/11/7.
 */
public interface ClassKit {

    static boolean isInstantiable(Class<?> clazz) {
        return isInstantiable(clazz, Object.class);
    }

    static boolean isInstantiable(Class<?> clazz, Class<?> superClazz) {
        return !clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers()) &&
            superClazz.isAssignableFrom(clazz);
    }
}
