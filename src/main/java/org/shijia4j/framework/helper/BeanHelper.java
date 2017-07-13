package org.shijia4j.framework.helper;

import org.shijia4j.framework.util.ReflectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by shijia on 7/13/17.
 */
public final class BeanHelper {
  private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<>();

  static {
    Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
    for(Class<?> beanClass : beanClassSet) {
      Object obj = ReflectionUtils.newInstance(beanClass);
      BEAN_MAP.put(beanClass, obj);
    }
  }

  public static Map<Class<?>, Object> getBeanMap() {
    return BEAN_MAP;
  }

  @SuppressWarnings("unchecked")
  public static <T> T getBean(Class<?> clazz) {
    if(!BEAN_MAP.containsKey(clazz)) {
      throw new RuntimeException("Can not get bean by class:" + clazz);
    }
    return (T) BEAN_MAP.get(clazz);
  }
}
