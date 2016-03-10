package com.huaren.logistics.common;

public enum OrderStatusEnum {
  READEY_CARGO("1", "待装车"), CARGO("2", "已装车"), UNCARGO("3", "已卸车"), EVALUATION(
      "4", "已评价");

  private String status;
  private String desc;

  public String getStatus() {
    return status;
  }

  public String getDesc() {
    return desc;
  }

  OrderStatusEnum(String status, String desc) {
    this.status = status;
    this.desc = desc;
  }
}
