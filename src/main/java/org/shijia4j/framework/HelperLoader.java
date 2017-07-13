package org.shijia4j.framework;

import org.shijia4j.framework.helper.BeanHelper;
import org.shijia4j.framework.helper.ClassHelper;
import org.shijia4j.framework.helper.ControllerHelper;
import org.shijia4j.framework.helper.IocHelper;
import org.shijia4j.framework.util.ClassUtils;

/**
 * Created by shijia on 7/13/17.
 */
public final class HelperLoader {
  public static void init() {
    Class<?>[] classList = {ClassHelper.class, BeanHelper.class, IocHelper.class, ControllerHelper.class};
    for(Class<?> clazz : classList) {
      ClassUtils.loadClass(clazz.getName(), true);
    }
  }
}
