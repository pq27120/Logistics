package com.huaren.logistics.entity;

import java.util.List;

public class Customer {
  private String name;
  private String code;
  private String address;
  /**
   * 未装车货物
   */
  private List<Goods> unloadedGoodsList;
  /**
   * 已装车货物
   */
  private List<Goods> loadedGoodsList;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public List<Goods> getUnloadedGoodsList() {
    return unloadedGoodsList;
  }

  public void setUnloadedGoodsList(List<Goods> unloadedGoodsList) {
    this.unloadedGoodsList = unloadedGoodsList;
  }

  public List<Goods> getLoadedGoodsList() {
    return loadedGoodsList;
  }

  public void setLoadedGoodsList(List<Goods> loadedGoodsList) {
    this.loadedGoodsList = loadedGoodsList;
  }
}
