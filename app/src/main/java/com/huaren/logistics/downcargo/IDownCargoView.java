package com.huaren.logistics.downcargo;

public interface IDownCargoView {
  void showProgress();

  void hideProgress();

  void setAdapter(DownCargoAdapter adapter);
}
