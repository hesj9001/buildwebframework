package org.shijia4j.chapter2.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.shijia4j.chapter2.helper.DatabaseHelper;
import org.shijia4j.chapter2.model.Customer;
import org.shijia4j.chapter2.service.CustomerService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shijia on 7/8/17.
 */
public class CustomerServletTest {
  private final CustomerService customerService;

  public CustomerServletTest() {
    this.customerService = new CustomerService();
  }

  @Before
  public void init() throws IOException {
    String file = "sql/customer_init.sql";
    DatabaseHelper.executeSqlFile(file);
  }

  @Test
  public void getCustomerListTest() {
    List<Customer> customers = customerService.getCustomerList(null);
    Assert.assertEquals(2, customers.size());
  }

  @Test
  public void getCustomerTest() {
    long id = 1;
    Customer customer = customerService.getCustomer(id);
    Assert.assertNotNull(customer);
  }

  @Test
  public void createCustomerTest() {
    Map<String, Object> fieldMap = new HashMap<>();
    fieldMap.put("name", "customer100");
    fieldMap.put("contact", "Jhon");
    fieldMap.put("telephone", "138999999");
    Assert.assertTrue(customerService.createCustomer(fieldMap));
  }

  @Test
  public void updateCustomerTest() {
    long id = 1;
    Map<String, Object> fieldMap = new HashMap<>();
    fieldMap.put("name", "king");
    Assert.assertTrue(customerService.updateCustomer(1, fieldMap));
  }

  @Test
  public void deleteCustomerTest() {
    long id = 2;
    Map<String, Object> fieldMap = new HashMap<>();
    Assert.assertTrue(customerService.deleteCustomer(id));
  }
}
