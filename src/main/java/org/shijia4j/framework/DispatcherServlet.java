package org.shijia4j.framework;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.shijia4j.framework.bean.Data;
import org.shijia4j.framework.bean.Handler;
import org.shijia4j.framework.bean.Param;
import org.shijia4j.framework.bean.View;
import org.shijia4j.framework.helper.BeanHelper;
import org.shijia4j.framework.helper.ConfigHelper;
import org.shijia4j.framework.helper.ControllerHelper;
import org.shijia4j.framework.util.CodecUtil;
import org.shijia4j.framework.util.JsonUtils;
import org.shijia4j.framework.util.ReflectionUtils;
import org.shijia4j.framework.util.StreamUtils;

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
import java.util.Enumeration;
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
  }

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String requestMethod = request.getMethod().toLowerCase();
    String requestPath = request.getPathInfo();
    Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
    if (handler != null) {
      Class<?> controllerClass = handler.getControllerClass();
      Object controllerBean = BeanHelper.getBean(controllerClass);
      Map<String, Object> paramMap = new HashMap<>();
      Enumeration<String> paramNames = request.getParameterNames();
      while (paramNames.hasMoreElements()) {
        String paramName = paramNames.nextElement();
        String paramValue = request.getParameter(paramName);
        paramMap.put(paramName, paramValue);
      }
      String body = CodecUtil.decodeURL(StreamUtils.getString(request.getInputStream()));
      if (StringUtils.isNotEmpty(body)) {
        String[] params = body.split("&");
        if (ArrayUtils.isNotEmpty(params)) {
          for (String param : params) {
            String[] array = param.split("=");
            if (ArrayUtils.isNotEmpty(array) && array.length == 2) {
              String paramName = array[0];
              String paramValue = array[1];
              paramMap.put(paramName, paramValue);
            }
          }
        }
      }
      Param param = new Param(paramMap);
      Method actionMethod = handler.getActionMethod();
      Object result = ReflectionUtils.invokeMethod(controllerBean, actionMethod, param);
      if (result instanceof View) {
        View view = (View) result;
        String path = view.getPath();
        if (StringUtils.isNotEmpty(path)) {
          if (path.startsWith("/")) {
            response.sendRedirect(request.getContextPath() + path);
          } else {
            Map<String, Object> model = view.getModel();
            for (Map.Entry<String, Object> entry : model.entrySet()) {
              request.setAttribute(entry.getKey(), entry.getValue());
            }
            request.getRequestDispatcher(ConfigHelper.getAppJspPath() + path)
                .forward(request, response);
          }
        }
      } else if (result instanceof Data) {
        Data data = (Data) result;
        Object model = data.getModel();
        if (model != null) {
          response.setContentType("application/json");
          response.setCharacterEncoding("UTF-8");
          PrintWriter writer = response.getWriter();
          String json = JsonUtils.toJson(model);
          writer.write(json);
          writer.flush();
          writer.close();
        }
      }
    }
  }
}
