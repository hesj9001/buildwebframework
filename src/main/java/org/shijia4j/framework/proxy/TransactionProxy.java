package org.shijia4j.framework.proxy;

import org.shijia4j.framework.annotation.Transaction;
import org.shijia4j.framework.helper.DatabaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by shijia on 7/16/17.
 */
public class TransactionProxy implements Proxy {
  private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProxy.class);

  private static final ThreadLocal<Boolean> FLAG_HOLDER = new ThreadLocal<Boolean>() {
    @Override
    protected Boolean initialValue() {
      return false;
    }
  };

  @Override
  public Object doProxy(ProxyChain proxyChain) throws Throwable {
    Object result;
    boolean flag = FLAG_HOLDER.get();
    Method method = proxyChain.getTargetMethod();
    if(!flag && method.isAnnotationPresent(Transaction.class)) {
      FLAG_HOLDER.set(true);
      try {
        DatabaseHelper.beginTransaction();
        LOGGER.debug("begin transaction");
        result = proxyChain.doProxyChain();
        DatabaseHelper.commitTransaction();
        LOGGER.debug("commit transaction");
      } catch (Exception e) {
        DatabaseHelper.rollbackTransaction();
        LOGGER.debug("rollback transaction");
        throw e;
      } finally {
        FLAG_HOLDER.remove();
      }
    } else {
      result = proxyChain.doProxyChain();
    }
    return result;
  }
}
