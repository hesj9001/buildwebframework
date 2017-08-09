package org.shijia4j.framework;

import org.apache.commons.lang3.StringUtils;
import org.shijia4j.framework.bean.Data;
import org.shijia4j.framework.bean.Handler;
import org.shijia4j.framework.bean.Param;
import org.shijia4j.framework.bean.View;
import org.shijia4j.framework.helper.*;
import org.shijia4j.framework.util.JsonUtils;
import org.shijia4j.framework.util.ReflectionUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shijia on 7/13/17.
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {
  @Override
  public void init(ServletConfig config) throws ServletException {
    HelperLoader.init();
    ServletContext servletContext = config.getServletContext();
    ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
    jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");
    ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
    defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
    UploadHelper.init(servletContext);
  }

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    ServletHelper.init(request, response);
    try {
      String requestMethod = request.getMethod().toLowerCase();
      String requestPath = request.getPathInfo();
      if (requestPath.equals("/favicon.ico")) {
        return;
        HashMap
      }
      Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
      if (handler != null) {
        Class<?> controllerClass = handler.getControllerClass();
        Object controllerBean = BeanHelper.getBean(controllerClass);
        Param param;
        if (UploadHelper.isMultipart(request)) {
          param = UploadHelper.createParam(request);
        } else {
          param = RequestHelper.createParam(request);
        }
        Object result;
        Method actionMethod = handler.getActionMethod();
        if (param.isEmpty()) {
          result = ReflectionUtils.invokeMethod(controllerBean, actionMethod);
        } else {
          result = ReflectionUtils.invokeMethod(controllerBean, actionMethod, param);
        }
        if (result instanceof View) {
          handleViewResult((View) result, request, response);
        } else if (result instanceof Data) {
          handleDataResult((Data) result, request, response);
        }
      }
    }finally {
      ServletHelper.destory();
    }
  }

  private void handleViewResult(View view, HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String path = view.getPath();
    if(StringUtils.isNotEmpty(path)) {
      if(path.startsWith("/")) {
        response.sendRedirect(request.getContextPath() + path);
      } else {
        Map<String, Object> model = view.getModel();
        for(Map.Entry<String, Object> entry : model.entrySet()) {
          request.setAttribute(entry.getKey(), entry.getValue());
        }
        request.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(request, response);
      }
    }
  }

  private void handleDataResult(Data data, HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    Object model = data.getModel();
    if(model != null) {
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      PrintWriter printWriter = response.getWriter();
      String json = JsonUtils.toJson(model);
      printWriter.write(json);
      printWriter.flush();
      printWriter.close();
    }
  }
}
