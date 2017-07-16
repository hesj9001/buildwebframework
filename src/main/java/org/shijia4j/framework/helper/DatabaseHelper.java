package org.shijia4j.framework.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by shijia on 7/16/17.
 */
public final class DatabaseHelper {
  private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);

  private static ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<>();

  public static void beginTransaction() {
    Connection conn = getConnection();
    if(conn != null) {
      try {
        conn.setAutoCommit(false);
      } catch (SQLException e) {
        LOGGER.error("begin tansaction failure", e);
        throw new RuntimeException(e);
      } finally {
        CONNECTION_HOLDER.set(conn);
      }
    }
  }

  public static void commitTransaction() {
    Connection conn = getConnection();
    if(conn != null) {
      try {
        conn.commit();
        conn.close();
      } catch (SQLException e) {
        LOGGER.error("commit transaction failure", e);
        throw new RuntimeException(e);
      }finally {
        CONNECTION_HOLDER.remove();
      }
    }
  }

  public static void rollbackTransaction() {
    Connection conn = getConnection();
    if(conn != null) {
      try {
        conn.rollback();
        conn.close();
      } catch (SQLException e) {
        LOGGER.error("rollback transaction failure", e);
        throw new RuntimeException(e);
      }finally {
        CONNECTION_HOLDER.remove();
      }
    }
  }

  private static Connection getConnection() {
    Connection conn = CONNECTION_HOLDER.get();
    try {
      if (conn == null) {
        Class.forName(ConfigHelper.getJdbcDriver());
        conn = DriverManager.getConnection(ConfigHelper.getJdbUrl(), ConfigHelper.getJdbcUsername(),
            ConfigHelper.getJdbcPassword());
      }
    }catch (Exception e) {
      LOGGER.error("fail to get connection", e);
      throw new RuntimeException(e);
    } finally {
      CONNECTION_HOLDER.set(conn);
    }
    return conn;
  }

  private static void closeConnection() {
    Connection conn = CONNECTION_HOLDER.get();
    try {
      if(conn != null) {
        conn.close();
      }
    }catch (Exception e) {
      LOGGER.error("fail to close connection");
    } finally {
      CONNECTION_HOLDER.remove();
    }
  }
}
