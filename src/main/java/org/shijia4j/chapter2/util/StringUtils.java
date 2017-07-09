package org.shijia4j.chapter2.util;

/**
 * Created by shijia on 7/9/17.
 */
public class StringUtils {
  public static boolean isEmpty(String string) {
    boolean retVal = false;
    if(string == null || string.length() == 0) {
      retVal = true;
    }
    return retVal;
  }

  public static boolean isNotEmpty(String string) {
    return !isEmpty(string);
  }
}
