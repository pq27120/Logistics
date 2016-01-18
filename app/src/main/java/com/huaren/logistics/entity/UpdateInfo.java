package com.huaren.logistics.entity;

public class UpdateInfo {
  private String version;
  private String code;
  private String description;
  private String bak;
  private String apkurl;
  private String isupdate;

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getBak() {
    return bak;
  }

  public void setBak(String bak) {
    this.bak = bak;
  }

  public String getApkurl() {
    return apkurl;
  }

  public void setApkurl(String apkurl) {
    this.apkurl = apkurl;
  }

  public String getIsupdate() {
    return isupdate;
  }

  public void setIsupdate(String isupdate) {
    this.isupdate = isupdate;
  }
}
