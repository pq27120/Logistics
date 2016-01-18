package com.huaren.logistics.splash;

import com.huaren.logistics.entity.UpdateInfo;

public interface ISplashView {
  void showUpdateProgress();

  void hideProgress();

  void showUpdateDialog(UpdateInfo updateInfo);

  void finishActivity();

  void changeProgress(int total);

  void enterMain();

  void enterLogin();
}
