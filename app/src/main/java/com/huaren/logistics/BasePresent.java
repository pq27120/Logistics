package com.huaren.logistics;

import android.content.Context;
import com.huaren.logistics.util.CommonTool;

public class BasePresent {
  private IBaseView baseView;

  public BasePresent(IBaseView baseView) {
    this.baseView = baseView;
  }

  public void initUserInfo() {
    String curName = CommonTool.getSharePreference((Context) baseView, "curUserName");
    baseView.setUserInfo(curName);
  }
}
