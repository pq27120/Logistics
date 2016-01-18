package com.huaren.logistics.downcargo;

public interface IDownCargoView {
  void showProgress();

  void hideProgress();

  void setAdapter(DownCargoAdapter adapter);

  void hideUpdateView();

  void showUpdateView();

  boolean isFooterShown();

  void onRefreshComplete();

  void initPullToRefreshListView(DownCargoAdapter adapter);
}
