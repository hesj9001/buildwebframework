package org.shijia4j.framework.helper;

import org.shijia4j.framework.annotation.Controller;
import org.shijia4j.framework.annotation.Service;
import org.shijia4j.framework.util.ClassUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by shijia on 7/12/17.
 */
public class ClassHelper {
  private static final Set<Class<?>> CLASS_SET;

  static {
    String basePackage = ConfigHelper.getAppBasePackage();
    CLASS_SET = ClassUtils.getClassSet(basePackage);
  }

  public static Set<Class<?>> getClassSet() {
    return CLASS_SET;
  }

  public static Set<Class<?>> getServiceClassSet() {
    Set<Class<?>> classSet = new HashSet<>();
    for(Class<?> clazz : CLASS_SET) {
      if(clazz.isAnnotationPresent(Service.class)) {
        classSet.add(clazz);
      }
    }
    return classSet;
  }

  public static Set<Class<?>> getControllerClassSet() {
    Set<Class<?>> classSet = new HashSet<>();
    for(Class<?> clazz : CLASS_SET) {
      if(clazz.isAnnotationPresent(Controller.class)) {
        classSet.add(clazz);
      }
    }
    return classSet;
  }

  public static Set<Class<?>> getBeanClassSet() {
    Set<Class<?>> beanClassSet = new HashSet<>();
    beanClassSet.addAll(getServiceClassSet());
    beanClassSet.addAll(getControllerClassSet());
    return beanClassSet;
  }
}
