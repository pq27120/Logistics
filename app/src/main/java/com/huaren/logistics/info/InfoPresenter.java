package com.huaren.logistics.info;

import android.content.Context;
import com.huaren.logistics.util.CommonTool;

public class InfoPresenter {
  private InfoView infoView;

  public InfoPresenter(InfoView infoView) {
    this.infoView = infoView;
  }

  public void clearUserInfo() {
    CommonTool.setSharePreference((Context) infoView, "isLogin", "false");
    CommonTool.setSharePreference((Context) infoView, "curUserName", "");
    CommonTool.setSharePreference((Context) infoView, "driverId", "");
    infoView.enterLogin();
  }
}
