package com.huaren.logistics.downcargo;

import android.content.res.AssetManager;

public interface IDownCargoView {
  void setAdapter(DownCargoAdapter adapter);

  void hideUpdateView();

  void showUpdateView(String time, String info);

  AssetManager getAssetManager();

  void finishActivity();
}
