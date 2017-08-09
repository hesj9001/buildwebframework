package org.shijia4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by shijia on 7/18/17.
 */
public class StreamUtil {
  private static final Logger LOGGER = LoggerFactory.getLogger(StreamUtil.class);

  public static void copyStream(InputStream inputStream, OutputStream outputStream) {
    try {
      int length;
      byte[] buffer = new byte[4 * 1024];
      while((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
        outputStream.write(buffer, 0, length);
      }
      outputStream.flush();
    } catch (Exception e) {
      LOGGER.error("copy stream failure", e);
      throw new RuntimeException(e);
    } finally {
      try {
        inputStream.close();
        outputStream.close();
      } catch (Exception e) {
        LOGGER.error("cloase stream failure", e);
      }
    }
  }
}
