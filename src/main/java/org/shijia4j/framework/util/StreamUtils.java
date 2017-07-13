package org.shijia4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by shijia on 7/13/17.
 */
public final class StreamUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(StreamUtils.class);

  public static String getString(InputStream is) {
    StringBuilder sb = new StringBuilder();
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      String line;
      while((line = reader.readLine()) != null) {
        sb.append(line);
      }
    }catch (Exception e) {
      LOGGER.error("got string failure", e);
      throw new RuntimeException(e);
    }
    return sb.toString();
  }
}
