package com.sedsoftware.comicser.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ClassUtils {

  /**
   * Returns methods list for AutoValue classes.
   *
   * @param type Class type.
   * @return String which contains comma-separated method names.
   */

  public static String getMethodsList(Class<?> type) {

    final String SEPARATOR = ",";
    final List<Method> methods = Arrays.asList(type.getDeclaredMethods());
    StringBuilder result = new StringBuilder();

    // Forced methods list sorting
    Collections.sort(methods, (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));

    for (Method method : methods) {
      int modifiers = method.getModifiers();
      if (Modifier.isAbstract(modifiers)) {
        result.append(method.getName()).append(SEPARATOR);
      }
    }

    return result.deleteCharAt(result.length() - 1).toString();
  }
}