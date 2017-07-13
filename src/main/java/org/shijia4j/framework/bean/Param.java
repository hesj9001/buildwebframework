package org.shijia4j.framework.bean;

import org.shijia4j.framework.util.CastUtils;

import java.util.Map;

/**
 * Created by shijia on 7/13/17.
 */
public class Param {

  private Map<String, Object> paramMap;

  public Param(Map<String, Object> paramMap) {
    this.paramMap = paramMap;
  }

  public long getLong(String name) {
    return CastUtils.castLong(name);
  }

  public Map<String, Object> getMap() {
    return paramMap;
  }
}
