package com.huaren.logistics.downcargo;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.R;
import com.huaren.logistics.util.CommonTool;

public class DownCargoActivity extends BaseActivity implements IDownCargoView {

  private PullToRefreshListView ptrlvDateInfo = null;

  private TextView lastUpdateTv;

  private LinearLayout lastUpdateLl;

  private DownCargoPresenter presenter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_downcargo);
    ptrlvDateInfo = (PullToRefreshListView) findViewById(R.id.down_cargo_list);
    lastUpdateTv = (TextView) findViewById(R.id.last_update_tv);
    lastUpdateLl = (LinearLayout) findViewById(R.id.last_update_ll);
    ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.down_cargo);
    presenter = new DownCargoPresenter(this);
    presenter.initList(false);
  }

  @Override public void showProgress() {

  }

  @Override public void hideProgress() {

  }

  @Override public void setAdapter(DownCargoAdapter adapter) {
    ptrlvDateInfo.setAdapter(adapter);
  }

  @Override public void hideUpdateView() {
    lastUpdateLl.setVisibility(View.GONE);
  }

  @Override public void showUpdateView() {
    lastUpdateLl.setVisibility(View.VISIBLE);
    lastUpdateTv.setText(CommonTool.parseCurrDateToString("yyyy-MM-dd HH:mm:ss"));
  }

  @Override public boolean isFooterShown() {
    return ptrlvDateInfo.isFooterShown();
  }

  @Override public void onRefreshComplete() {
    ptrlvDateInfo.onRefreshComplete();
  }

  /**
   * 初始化PullToRefresh
   */
  public void initPullToRefreshListView(DownCargoAdapter adapter) {
    ptrlvDateInfo.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
    ptrlvDateInfo.setOnRefreshListener(new MyOnRefreshListener(ptrlvDateInfo));
    ptrlvDateInfo.setAdapter(adapter);
  }

  class MyOnRefreshListener implements
      PullToRefreshBase.OnRefreshListener2<ListView> {

    private PullToRefreshListView mPtflv;

    public MyOnRefreshListener(PullToRefreshListView ptflv) {
      this.mPtflv = ptflv;
    }

    @Override public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
      // 上拉加载
      switch (mPtflv.getId()) {
        case R.id.down_cargo_list:
          presenter.initList(true);
          break;
      }
    }
  }
}
