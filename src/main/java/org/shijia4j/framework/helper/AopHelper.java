package org.shijia4j.framework.helper;

import org.shijia4j.framework.annotation.Aspect;
import org.shijia4j.framework.annotation.Transaction;
import org.shijia4j.framework.proxy.AspectProxy;
import org.shijia4j.framework.proxy.Proxy;
import org.shijia4j.framework.proxy.ProxyManager;
import org.shijia4j.framework.proxy.TransactionProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Created by shijia on 7/16/17.
 */
public final class AopHelper {
  private static final Logger LOGGER = LoggerFactory.getLogger(AopHelper.class);

  static {
    try{
      Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
      Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
      for(Map.Entry<Class<?>, List<Proxy>> targetEntity : targetMap.entrySet()) {
        Class<?> targetClass = targetEntity.getKey();
        List<Proxy> proxies = targetEntity.getValue();
        Object proxy = ProxyManager.createProxy(targetClass, proxies);
        BeanHelper.setBean(targetClass, proxy);
      }
    } catch (Exception e) {
      LOGGER.error("aop failure", e);
    }
  }
  private static Set<Class<?>> createTargetClassSet(Aspect aspect) {
    Set<Class<?>> targetClassSet = new HashSet<>();
    Class<? extends Annotation> annotation = aspect.value();
    if(annotation != null && !annotation.equals(Aspect.class)) {
      targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
    }
    return targetClassSet;
  }

  private static Map<Class<?>, Set<Class<?>>> createProxyMap() {
    Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<>();
    addAspectProxy(proxyMap);
    addTransactionProxy(proxyMap);
    return proxyMap;
  }

  private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> proxyMap)
      throws Exception {
    Map<Class<?>, List<Proxy>> targetMap = new HashMap<>();
    for(Map.Entry<Class<?>, Set<Class<?>>> proxyEntry : proxyMap.entrySet()) {
      Class<?> proxyClass = proxyEntry.getKey();
      Set<Class<?>> targetClassSet = proxyEntry.getValue();
      for(Class<?> targetClass : targetClassSet) {
        Proxy proxy = (Proxy) proxyClass.newInstance();
        if(targetMap.containsKey(targetClass)) {
          targetMap.get(targetClass).add(proxy);
        } else {
          List<Proxy> proxyList = new ArrayList<>();
          proxyList.add(proxy);
          targetMap.put(targetClass, proxyList);
        }
      }
    }
    return targetMap;
  }

  private static void addAspectProxy(Map<Class<?>, Set<Class<?>>> proxyMap) {
    Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);
    for(Class<?> proxyClass : proxyClassSet) {
      if(proxyClass.isAnnotationPresent(Aspect.class)) {
        Aspect aspect = proxyClass.getAnnotation(Aspect.class);
        Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
        proxyMap.put(proxyClass, targetClassSet);
      }
    }
  }

  private static void addTransactionProxy(Map<Class<?>, Set<Class<?>>> proxyMap) {
    Set<Class<?>> proxyClassSet = ClassHelper.getClassSetByAnnotation(Transaction.class);
    proxyMap.put(TransactionProxy.class, proxyClassSet);
  }

}
