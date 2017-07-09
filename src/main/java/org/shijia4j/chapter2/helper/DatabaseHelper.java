package org.shijia4j.chapter2.helper;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.shijia4j.chapter2.util.PropsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by shijia on 7/9/17.
 */
public class DatabaseHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);

  private static final BasicDataSource DATA_SOURCE;

  private static final QueryRunner QUERY_RUNNER = new QueryRunner();

  private static final ThreadLocal<Connection> CONNECTION_CONTAINER = new ThreadLocal<>();

  static {
    Properties properties = PropsUtils.loadProps("config.properties");
    String driver = properties.getProperty("jdbc.driver");
    String url = properties.getProperty("jdbc.url");
    String username = properties.getProperty("jdbc.username");
    String password = properties.getProperty("jdbc.password");

    DATA_SOURCE = new BasicDataSource();
    DATA_SOURCE.setDriverClassName(driver);
    DATA_SOURCE.setUrl(url);
    DATA_SOURCE.setUsername(username);
    DATA_SOURCE.setPassword(password);

  }

  public static Connection getConnection() {
    Connection connection = CONNECTION_CONTAINER.get();
    if(connection == null) {
      try {
        connection = DATA_SOURCE.getConnection();
        CONNECTION_CONTAINER.set(connection);
      } catch (SQLException e) {
        LOGGER.error("get connection failed", e);
      }
    }
    return connection;
  }

  public static <T>List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
    List<T> entityList;
    try {
      Connection connection = getConnection();
      entityList = QUERY_RUNNER.query(connection, sql, new BeanListHandler<T>(entityClass), params);
    } catch (SQLException e) {
      LOGGER.error("query entity list failed", e);
      throw new RuntimeException(e);
    }
    return entityList;
  }

  public static <T>T queryEntity(Class<T> entityClass, String sql, Object... params) {
    T entity;
    try {
      Connection connection = getConnection();
      entity = QUERY_RUNNER.query(connection, sql, new BeanHandler<T>(entityClass), params);
    } catch (SQLException e) {
      LOGGER.error("query entity failed", e);
      throw new RuntimeException(e);
    }
    return entity;
  }

  public static List<Map<String, Object>> executeQuery(String sql, Object... params) {
    List<Map<String, Object>> result;
    try {
      Connection connection = getConnection();
      result = QUERY_RUNNER.query(connection, sql, new MapListHandler(), params);
    } catch (Exception e) {
      LOGGER.error("execute query failed", e);
      throw new RuntimeException(e);
    }
    return result;
  }

  public static int executeUpdate(String sql, Object... params) {
    int rows = 0;
    try {
      Connection connection = getConnection();
      rows = QUERY_RUNNER.update(connection, sql, params);
    } catch (Exception e) {
      LOGGER.error("execute query failed", e);
      throw new RuntimeException(e);
    }
    return rows;
  }

  public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap) {
    if(MapUtils.isEmpty(fieldMap)) {
      LOGGER.error("can not insert entity: fieldMap is empty");
      return false;
    }
    String sql = "INSERT INTO " + getTableName(entityClass);
    StringBuilder columns = new StringBuilder("(");
    StringBuilder values = new StringBuilder("(");
    fieldMap.forEach((fieldName, value) -> {
      columns.append(fieldName).append(", ");
      values.append("?, ");
    });
    columns.replace(columns.lastIndexOf(", "), columns.length(), ")");
    values.replace(values.lastIndexOf(", "), values.length(), ")");
    sql += columns + " VALUES " + values;
    Object[] params = fieldMap.values().toArray();
    return executeUpdate(sql, params) == 1;
  }

  public static boolean updateEntity(Class entityClass, long id, Map<String, Object> fieldMap) {
    if(MapUtils.isEmpty(fieldMap)) {
      LOGGER.error("can not update entity: fieldMap is empty");
      return false;
    }

    String sql = "UPDATE " + getTableName(entityClass) + " SET ";
    StringBuilder columns = new StringBuilder();
    fieldMap.forEach((fieldName, value) -> {
      columns.append(fieldName).append("=?, ");
    });
    sql += columns.substring(0, columns.lastIndexOf(", ")) + " WHERE id=?";
    List<Object> paramsList = new ArrayList<>();
    paramsList.addAll(fieldMap.values());
    paramsList.add(id);
    Object[] params = paramsList.toArray();

    return executeUpdate(sql, params) == 1;
  }

  public static boolean deleteEntity(Class entityClass, long id) {
    String sql = "DELETE FROM " + getTableName(entityClass) + " WHERE id=?";
    return executeUpdate(sql, id) == 1;
  }

  public static void executeSqlFile(String filePath) {
    InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    try {
      String sql;
      while((sql=bufferedReader.readLine()) != null) {
        executeUpdate(sql);
      }
    } catch (Exception e) {
      LOGGER.error("execute sql file failed", e);
      throw new RuntimeException(e);
    }
  }

  private static String getTableName(Class clazz) {
    return clazz.getSimpleName().toLowerCase();
  }
}
