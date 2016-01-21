package com.huaren.logistics.downcargo;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.OnButtonClickListener;
import com.dexafree.materialList.card.provider.BasicButtonsCardProvider;
import com.dexafree.materialList.view.MaterialListView;
import com.gc.materialdesign.views.ButtonRectangle;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.R;
import com.huaren.logistics.util.CommonTool;

public class DownCargoActivity extends BaseActivity implements IDownCargoView {

  //private PullToRefreshListView ptrlvDateInfo = null;

  private ButtonRectangle updateButton;

  private DownCargoPresenter presenter;

  private MaterialListView mListView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_downcargo);
    //ptrlvDateInfo = (PullToRefreshListView) findViewById(R.id.down_cargo_list);
    mListView = (MaterialListView) findViewById(R.id.down_cargo_listview);
    updateButton = (ButtonRectangle) findViewById(R.id.btn_update);
    ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.down_cargo);
    presenter = new DownCargoPresenter(this);
    presenter.downloadData();
    updateButton.setOnClickListener(new UpdateButtonClick());
  }

  @Override public void showProgress() {

  }

  @Override public void hideProgress() {

  }

  @Override public void setAdapter(DownCargoAdapter adapter) {
    //ptrlvDateInfo.setAdapter(adapter);
  }

  @Override public void hideUpdateView() {
  }

  @Override public void showUpdateView(String time, String info) {
    Card card = new Card.Builder(this)
        .withProvider(BasicButtonsCardProvider.class)
        .setTitle("更新成功")
        .setDescription("更新时间：" + time + "," + info)
        .endConfig().build();
    mListView.add(card);
  }

  @Override public boolean isFooterShown() {
    //return ptrlvDateInfo.isFooterShown();
    return false;
  }

  @Override public void onRefreshComplete() {
    //ptrlvDateInfo.onRefreshComplete();
  }

  /**
   * 初始化PullToRefresh
   */
  public void initPullToRefreshListView(DownCargoAdapter adapter) {
    //ptrlvDateInfo.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
    //ptrlvDateInfo.setOnRefreshListener(new MyOnRefreshListener(ptrlvDateInfo));
    //ptrlvDateInfo.setAdapter(adapter);
  }

  @Override public AssetManager getAssetManager() {
    return getAssets();
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
      //switch (mPtflv.getId()) {
        //case R.id.down_cargo_list:
        //  presenter.initList(true);
        //  break;
      //}
    }
  }

  private class UpdateButtonClick implements View.OnClickListener {
    @Override public void onClick(View v) {
      presenter.downloadData();
    }
  }
}
