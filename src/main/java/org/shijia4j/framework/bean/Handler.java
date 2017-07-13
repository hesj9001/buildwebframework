package org.shijia4j.framework.bean;

import java.lang.reflect.Method;

/**
 * Created by shijia on 7/13/17.
 */
public class Handler {

  private Class<?> controllerClass;

  private Method actionMethod;

  public Handler(Class<?> controllerClass, Method actionMethod) {
    this.controllerClass = controllerClass;
    this.actionMethod = actionMethod;
  }

  public Class<?> getControllerClass() {
    return controllerClass;
  }

  public Method getActionMethod() {
    return actionMethod;
  }
}
