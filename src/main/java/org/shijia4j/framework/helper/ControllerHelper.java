package org.shijia4j.framework.helper;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.shijia4j.framework.annotation.Action;
import org.shijia4j.framework.bean.Handler;
import org.shijia4j.framework.bean.Request;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by shijia on 7/13/17.
 */
public final class ControllerHelper {

  private static final Map<Request, Handler> ACTION_MAP = new HashMap<>();

  static {
    Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
    if(CollectionUtils.isNotEmpty(controllerClassSet)) {
      for(Class<?> controllerClass : controllerClassSet) {
        Method[] methods = controllerClass.getDeclaredMethods();
        if(ArrayUtils.isNotEmpty(methods)) {
          for(Method method : methods) {
            if(method.isAnnotationPresent(Action.class)) {
              Action action = method.getAnnotation(Action.class);
              String mapping = action.value();
              if(mapping.matches("\\w+:\\w*")) {
                String[] array = mapping.split(".");
                if(ArrayUtils.isNotEmpty(array) && array.length == 2) {
                  String requestMethod = array[0];
                  String requsetPath = array[1];
                  Request request = new Request(requestMethod, requsetPath);
                  Handler handler = new Handler(controllerClass, method);
                  ACTION_MAP.put(request, handler);
                }
              }
            }
          }
        }
      }
    }
  }

  public static Handler getHandler(String requestMethod, String requestPath) {
    Request request = new Request(requestMethod, requestPath);
    return ACTION_MAP.get(request);
  }
}
