package org.shijia4j.chapter2.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by shijia on 7/9/17.
 */
public class PropsUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(PropsUtils.class);

  public static Properties loadProps(String fileName) {
    Properties properties = null;
    InputStream in = null;
    try {
      in = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
      if(in == null) {
        throw new FileNotFoundException(fileName + "file is not found");
      }
      properties = new Properties();
      properties.load(in);
    } catch (IOException e) {
      LOGGER.error("load properties file failed", e);
    } finally {
      if(in != null) {
        try {
          in.close();
        } catch (IOException e) {
          LOGGER.error("close input stream failed", e);
        }
      }
    }
    return properties;
  }

  public static String getString(Properties properties, String key) {
    return getString(properties, key, "");
  }

  public static String getString(Properties properties, String key, String defaultValue) {
    String value = defaultValue;
    if(properties.containsKey(key)) {
      value = properties.getProperty(key);
    }
    return value;
  }

  public static int getInteger(Properties properties, String key) {
    return getInteger(properties, key, 0);
  }

  public static int getInteger(Properties properties, String key, int defaultValue) {
    int value = defaultValue;
    if(properties.containsKey(key)) {
      value = Integer.class.cast(properties.getProperty(key));
    }
    return value;
  }

  public static boolean getBoolean(Properties properties, String key) {
    return getBoolean(properties, key, false);
  }

  public static boolean getBoolean(Properties properties, String key, boolean defaultValue) {
    boolean value = defaultValue;
    if(properties.containsKey(key)) {
      value = Boolean.class.cast(properties.getProperty(key));
    }
    return value;
  }
}
