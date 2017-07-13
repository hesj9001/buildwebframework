package org.shijia4j.framework.helper;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.shijia4j.framework.annotation.Inject;
import org.shijia4j.framework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by shijia on 7/13/17.
 */
public final class IocHelper {

  static {
    Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
    if(MapUtils.isNotEmpty(beanMap)) {
      for(Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
        Class<?> beanClass = beanEntry.getKey();
        Object beanInstance = beanEntry.getValue();
        Field[] beanFields = beanClass.getDeclaredFields();
        if(ArrayUtils.isNotEmpty(beanFields)) {
          for(Field beanField : beanFields) {
            if(beanField.isAnnotationPresent(Inject.class)) {
              Class<?> beanFieldClass = beanField.getType();
              Object beanFieldInstance = beanMap.get(beanFieldClass);
              if(beanFieldInstance != null) {
                ReflectionUtils.setFiled(beanInstance, beanField, beanFieldInstance);
              }
            }
          }
        }
      }
    }
  }
}
