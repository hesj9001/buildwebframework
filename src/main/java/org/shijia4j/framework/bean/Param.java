package org.shijia4j.framework.bean;

import org.apache.commons.collections4.CollectionUtils;
import org.shijia4j.framework.util.CastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shijia on 7/13/17.
 */
public class Param {

  private List<FormParam> formParamList;

  private List<FileParam> fileParamList;

  public Param(List<FormParam> formParamList) {
    this.formParamList = formParamList;
  }

  public Param(List<FormParam> formParamList, List<FileParam> fileParamList) {
    this.formParamList = formParamList;
    this.fileParamList = fileParamList;
  }

  public Map<String, Object> getFieldMap() {
    Map<String, Object> fieldMap = new HashMap<>();
    if(CollectionUtils.isNotEmpty(formParamList)) {
      for(FormParam formParam : formParamList) {
        String fieldName = formParam.getFieldName();
        Object fieldValue = formParam.getFieldValue();
        if(fieldMap.containsKey(fieldName)) {
          fieldValue = fieldMap.get(fieldName) + String.valueOf((char)29) + fieldValue;
        }
        fieldMap.put(fieldName, fieldValue);
      }
    }
    return fieldMap;
  }

  public Map<String, List<FileParam>> getFileMap() {
    Map<String, List<FileParam>> fileMap = new HashMap<>();
    if(CollectionUtils.isNotEmpty(fileParamList)) {
      for(FileParam fileParam : fileParamList) {
        String fieldName = fileParam.getFieldName();
        List<FileParam> fileParamList;
        if(fileMap.containsKey(fieldName)) {
          fileParamList = fileMap.get(fieldName);
        } else {
          fileParamList = new ArrayList<>();
        }
        fileParamList.add(fileParam);
        fileMap.put(fieldName, fileParamList);
      }
    }
    return fileMap;
  }

  public List<FileParam> getFileList(String fieldName) {
    return getFileMap().get(fieldName);
  }

  public FileParam getFile(String fieldName) {
    List<FileParam> fileParamList = getFileList(fieldName);
    if(CollectionUtils.isNotEmpty(fileParamList) && fileParamList.size() == 1) {
      return fileParamList.get(0);
    }
    return null;
  }

  public boolean isEmpty() {
    return CollectionUtils.isEmpty(formParamList) && CollectionUtils.isEmpty(fileParamList);
  }

  public String getString(String name) {
    return CastUtils.castString(getFieldMap().get(name));
  }

  public double getDouble(String name) {
    return CastUtils.castDouble(getFieldMap().get(name));
  }

  public long getLong(String name) {
    return CastUtils.castLong(getFieldMap().get(name));
  }

  public int getInt(String name) {
    return CastUtils.castInt(getFieldMap().get(name));
  }

  public boolean getBoolean(String name) {
    return CastUtils.castBoolean(getFieldMap().get(name));
  }
}
