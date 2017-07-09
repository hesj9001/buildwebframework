package org.shijia4j.chapter2.service;

import org.shijia4j.chapter2.helper.DatabaseHelper;
import org.shijia4j.chapter2.model.Customer;
import org.shijia4j.chapter2.util.PropsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by shijia on 7/8/17.
 */
public class CustomerService {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

  public List<Customer> getCustomerList(String keyword) {
    String sql = "select * from customer";
    return DatabaseHelper.queryEntityList(Customer.class, sql);
  }

  public Customer getCustomer(long id) {
    String sql = "select * from customer where id=?";
    return DatabaseHelper.queryEntity(Customer.class, sql, id);
  }

  public boolean createCustomer(Map<String, Object> fieldMap) {
    return DatabaseHelper.insertEntity(Customer.class, fieldMap);
  }

  public boolean updateCustomer(long id, Map<String, Object> fieldMap) {
    return DatabaseHelper.updateEntity(Customer.class, id, fieldMap);
  }

  public boolean deleteCustomer(long id) {
    return DatabaseHelper.deleteEntity(Customer.class, id);
  }
}
