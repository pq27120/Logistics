package com.huaren.logistics;

import android.content.Context;
import com.huaren.logistics.util.CommonTool;

public class BasePresent {
  private IBaseView baseView;

  public BasePresent(IBaseView baseView) {
    this.baseView = baseView;
  }

  public void initUserInfo() {
    String name = CommonTool.getSharePreference((Context) baseView, "name");
    String driver = CommonTool.getSharePreference((Context) baseView, "name");
    String licensePlate = CommonTool.getSharePreference((Context) baseView, "licensePlate");
    baseView.setUserInfo(name, driver, licensePlate);
  }
}
