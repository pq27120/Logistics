package com.huaren.logistics.uploadcargo;

import android.content.res.AssetManager;

public interface IUploadCargoView {
  void showProgress();

  void hideProgress();

  void hideUpdateView();

  void showUpdateView(String time, String info);

  AssetManager getAssetManager();

  void finishActivity();
}
