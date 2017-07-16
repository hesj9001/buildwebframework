package org.shijia4j.aspect;

import org.shijia4j.framework.annotation.Aspect;
import org.shijia4j.framework.annotation.Controller;
import org.shijia4j.framework.proxy.AspectProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by shijia on 7/16/17.
 */
@Aspect(Controller.class)
public class ControllerAspect extends AspectProxy{

  private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAspect.class);

  private long begin;

  @Override
  public void before(Class<?> clz, Method method, Object[] params) throws Throwable {
    LOGGER.debug("---------------begin------------");
    LOGGER.debug(String.format("class: %s", clz.getName()));
    LOGGER.debug(String.format("method: %s", method.getName()));
    begin = System.currentTimeMillis();
  }

  @Override
  public void after(Class<?> clz, Method method, Object[] params, Object result) throws Throwable {
    LOGGER.debug(String.format("time: %s", System.currentTimeMillis() - begin));
    LOGGER.debug("------------------end----------------");
  }
}
