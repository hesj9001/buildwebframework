package org.shijia4j.framework;

import org.shijia4j.framework.helper.*;
import org.shijia4j.framework.util.ClassUtils;

/**
 * Created by shijia on 7/13/17.
 */
public final class HelperLoader {
  public static void init() {
    Class<?>[] classList = {ClassHelper.class, BeanHelper.class, AopHelper.class, IocHelper.class, ControllerHelper.class};
    for(Class<?> clazz : classList) {
      ClassUtils.loadClass(clazz.getName(), true);
    }
  }
}
