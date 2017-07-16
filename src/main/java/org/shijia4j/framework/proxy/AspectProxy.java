package org.shijia4j.framework.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by shijia on 7/16/17.
 */
public abstract class AspectProxy implements Proxy{
  private static final Logger LOGGER = LoggerFactory.getLogger(AspectProxy.class);

  @Override
  public final Object doProxy(ProxyChain proxyChain) throws Throwable {
    Object result = null;
    Class<?> clz = proxyChain.getTargetClass();
    Method method = proxyChain.getTargetMethod();
    Object[] params = proxyChain.getMethodParams();

    begin();

    try {
      if (intercept(clz, method, params)) {
        before(clz, method, params);
        result = proxyChain.doProxyChain();
        after(clz, method, params, result);
      } else {
        result = proxyChain.doProxyChain();
      }
    } catch (Exception e) {
      LOGGER.error("proxy failure", e);
      error(clz, method, params, e);
      throw e;
    } finally {
      end();
    }
    return result;
  }

  public void begin() {

  }

  public boolean intercept(Class<?> clz, Method method, Object[] params) throws Throwable {
    return true;
  }

  public void before(Class<?> clz, Method method, Object[] params) throws Throwable {

  }

  public void after(Class<?> clz, Method method, Object[] params, Object result) throws Throwable {

  }

  public void error(Class<?> clz, Method method, Object[] params, Throwable e) {

  }

  public void end() {

  }
}
