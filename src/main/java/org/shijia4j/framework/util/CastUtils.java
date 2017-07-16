package org.shijia4j.framework.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * Created by shijia on 7/9/17.
 */
public class CastUtils {
  public static String castString(Object object) {
    return CastUtils.castString(object, "");
  }

  public static String castString(Object object, String defaultValue) {
    return Objects.isNull(object) ? defaultValue : String.valueOf(object);
  }

  public static double castDouble(Object object) {
    return CastUtils.castDouble(object, 0);
  }

  public static double castDouble(Object object, double defaultValue) {
    double value = defaultValue;
    if(Objects.isNull(object)) {
      String strValue = castString(object);
      if(StringUtils.isNotEmpty(strValue)) {
        try {
          value = Double.parseDouble(strValue);
        } catch (Exception e) {
          value = defaultValue;
        }
      }
    }
    return value;
  }

  public static long castLong(Object object) {
    return CastUtils.castLong(object, 0);
  }

  public static long castLong(Object object, long defaultValue) {
    long value = defaultValue;
    if(Objects.isNull(object)) {
      String strValue = castString(object);
      if(StringUtils.isNotEmpty(strValue)) {
        try {
          value = Long.parseLong(strValue);
        } catch (Exception e) {
          value = defaultValue;
        }
      }
    }
    return value;
  }

  public static int castInt(Object object) {
    return castInt(object, 0);
  }

  public static int castInt(Object object, int defaultValue) {
    int value = defaultValue;
    if(Objects.isNull(object)) {
      String strValue = castString(object);
      if(StringUtils.isNotEmpty(strValue)) {
        try {
          value = Integer.parseInt(strValue);
        } catch (Exception e) {
          value = defaultValue;
        }
      }
    }
    return value;
  }

  public static boolean castBoolean(Object object) {
    return castBoolean(object, false);
  }

  public static boolean castBoolean(Object object, boolean defaultValue) {
    boolean value = defaultValue;
    if(Objects.isNull(object)) {
      String strValue = castString(object);
      if(StringUtils.isNotEmpty(strValue)) {
        try {
          value = Boolean.parseBoolean(strValue);
        } catch (Exception e) {
          value = defaultValue;
        }
      }
    }
    return value;
  }
}
