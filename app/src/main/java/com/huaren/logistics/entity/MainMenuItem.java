package com.huaren.logistics.entity;

/**
 * 主页面菜单实体
 */
public class MainMenuItem {
  private String name;
  private String imageId;
  public Integer id;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getImageId() {
    return imageId;
  }

  public void setImageId(String imageId) {
    this.imageId = imageId;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }
}
