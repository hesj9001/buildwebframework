package org.shijia4j.framework.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shijia on 7/13/17.
 */
public final class JsonUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public static <T> String toJson(T obj) {
    String json;
    try {
      json = OBJECT_MAPPER.writeValueAsString(obj);
    } catch (Exception e) {
      LOGGER.error("convert POJO to json failure", e);
      throw new RuntimeException(e);
    }
    return json;
  }

  public static <T> T fromJson(String json, Class<T> type) {
    T pojo;
    try {
      pojo = OBJECT_MAPPER.readValue(json, type);
    } catch (Exception e) {
      LOGGER.error("convert JSON to POJO failure", e);
      throw new RuntimeException(e);
    }
    return pojo;
  }
}
