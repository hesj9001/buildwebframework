package org.shijia4j.framework.proxy;

/**
 * Created by shijia on 7/16/17.
 */
public interface Proxy {
  Object doProxy(ProxyChain proxyChain) throws Throwable;
}
