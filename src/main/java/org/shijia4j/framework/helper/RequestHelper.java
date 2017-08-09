package org.shijia4j.framework.helper;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.shijia4j.framework.bean.FormParam;
import org.shijia4j.framework.bean.Param;
import org.shijia4j.framework.util.CodecUtil;
import org.shijia4j.framework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by shijia on 7/18/17.
 */
public final class RequestHelper {
  public static Param createParam(HttpServletRequest request) throws IOException {
    List<FormParam> formParamList = new ArrayList<>();
    formParamList.addAll(parseParameterNames(request));
    formParamList.addAll(parseInputStream(request));
    return new Param(formParamList);
  }

  private static List<FormParam> parseParameterNames(HttpServletRequest request) {
    List<FormParam> formParamList = new ArrayList<>();
    Enumeration<String> paramNames = request.getParameterNames();
    while(paramNames.hasMoreElements()) {
      String fieldName = paramNames.nextElement();
      String[] fieldValues = request.getParameterValues(fieldName);
      if(ArrayUtils.isNotEmpty(fieldValues)) {
        Object fieldValue;
        if(fieldValues.length == 1) {
          fieldValue = fieldValues[0];
        } else {
          StringBuilder sb = new StringBuilder("");
          for(int i = 0; i < fieldValues.length; i++) {
            sb.append(fieldValues[i]);
            if(i != fieldValues.length - 1) {
              sb.append(String.valueOf((char)29));
            }
          }
          fieldValue = sb.toString();
        }
        formParamList.add(new FormParam(fieldName, fieldValue));
      }
    }
    return formParamList;
  }

  private static List<FormParam> parseInputStream(HttpServletRequest request) throws IOException {
    List<FormParam> formParamList = new ArrayList<>();
    String body = CodecUtil.decodeURL(StreamUtils.getString(request.getInputStream()));
    if(StringUtils.isNotEmpty(body)) {
      String[] kvs = StringUtils.split(body, "&");
      if(ArrayUtils.isNotEmpty(kvs)) {
        for(String kv : kvs) {
          String[] array = kv.split("=");
          if(ArrayUtils.isNotEmpty(array) && array.length == 2) {
            String fieldName = array[0];
            String fieldValue = array[1];
            formParamList.add(new FormParam(fieldName, fieldValue));
          }
        }
      }
    }
    return formParamList;
  }
}
