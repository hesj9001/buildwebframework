package org.shijia4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by shijia on 7/13/17.
 */
public class ReflectionUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtils.class);

  public static Object newInstance(Class<?> clazz) {
    Object instance;
    try {
      instance = clazz.newInstance();
    } catch (Exception e) {
      LOGGER.error("new instance failure", e);
      throw new RuntimeException(e);
    }
    return instance;
  }

  public static Object invokeMethod(Object object, Method method, Object... args) {
    Object result;
    try {
      method.setAccessible(true);
      result = method.invoke(object, args);
    } catch (Exception e) {
      LOGGER.error("invoke method failure", e);
      throw new RuntimeException(e);
    }
    return result;
  }

  public static void setFiled(Object object, Field field, Object value) {
    try {
      field.setAccessible(true);
      field.set(object, value);
    } catch (Exception e) {
      LOGGER.error("set field failure", e);
      throw new RuntimeException(e);
    }
  } 
}
