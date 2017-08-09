package org.shijia4j.framework.helper;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.shijia4j.framework.bean.FileParam;
import org.shijia4j.framework.bean.FormParam;
import org.shijia4j.framework.bean.Param;
import org.shijia4j.framework.util.FileUtil;
import org.shijia4j.framework.util.StreamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by shijia on 7/18/17.
 */
public final class UploadHelper {
  private static final Logger LOGGER = LoggerFactory.getLogger(UploadHelper.class);

  private static ServletFileUpload servletFileUpload;

  public static void init(ServletContext servletContext){
    File repository = (File)servletContext.getAttribute("javax.servlet.context.tempdir");
    servletFileUpload = new ServletFileUpload(new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repository));
    int uploadLimit = ConfigHelper.getAppUploadLimit();
    if(uploadLimit != 0) {
      servletFileUpload.setFileSizeMax(uploadLimit * 1024 * 1024);
    }
  }

  public static boolean isMultipart(HttpServletRequest httpServletRequest) {
    return ServletFileUpload.isMultipartContent(httpServletRequest);
  }

  public static Param createParam(HttpServletRequest request) throws IOException {
    List<FormParam> formParamList = new ArrayList<>();
    List<FileParam> fileParamList = new ArrayList<>();
    try {
      Map<String, List<FileItem>> fileItemListMap = servletFileUpload.parseParameterMap(request);
      if(MapUtils.isNotEmpty(fileItemListMap)) {
        for(Map.Entry<String, List<FileItem>> fileItemListEntry : fileItemListMap.entrySet()) {
          String fieldName = fileItemListEntry.getKey();
          List<FileItem> fileItemList = fileItemListEntry.getValue();
          if(CollectionUtils.isNotEmpty(fileItemList)) {
            for(FileItem fileItem : fileItemList) {
              if(fileItem.isFormField()) {
                String fieldValue = fileItem.getString("UTF-8");
                formParamList.add(new FormParam(fieldName, fieldValue));
              } else {
                String fileName = FileUtil.getRealFileName(new String(fileItem.getName().getBytes(), "UTF-8"));
                if(StringUtils.isNotEmpty(fileName)) {
                  long fileSize = fileItem.getSize();
                  String contentType = fileItem.getContentType();
                  InputStream inputStream = fileItem.getInputStream();
                  fileParamList.add(new FileParam(fieldName, fileName, fileSize, contentType, inputStream));
                }
              }
            }
          }
        }
      }
    }catch (FileUploadException e) {
      LOGGER.error("create param failure", e);
      throw new RuntimeException(e);
    }
    return new Param(formParamList, fileParamList);
  }

  public static void uploadFile(String basePath, FileParam fileParam) {
    try {
      if(fileParam != null) {
        String filePath = basePath + fileParam.getFileName();
        FileUtil.createFile(filePath);
        InputStream inputStream = new BufferedInputStream(fileParam.getInputStream());
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
        StreamUtil.copyStream(inputStream, outputStream);
      }
    }catch (Exception e) {
      LOGGER.error("upload file failure", e);
      throw new RuntimeException(e);
    }
  }

  public static void uploadFile(String basePath, List<FileParam> fileParamList) {
    try {
      if(CollectionUtils.isNotEmpty(fileParamList)) {
        for(FileParam fileParam : fileParamList) {
          uploadFile(basePath, fileParam);
        }
      }
    } catch (Exception e) {
      LOGGER.error("upload file failure", e);
      throw new RuntimeException(e);
    }
  }
}
