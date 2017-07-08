package org.shijia4j.chapter2.model;

import java.io.Serializable;

/**
 * Created by shijia on 7/8/17.
 */
public class Customer implements Serializable{

  private static final long serialVersionUID = 3167471070315598304L;

  private long id;

  private String name;

  private String contact;

  private String telephone;

  private String email;

  private String remark;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }
}
