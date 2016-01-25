package com.huaren.logistics.downcargo;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.BasicButtonsCardProvider;
import com.dexafree.materialList.view.MaterialListView;
import com.gc.materialdesign.views.ButtonRectangle;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.R;

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

  @Override public AssetManager getAssetManager() {
    return getAssets();
  }

  private class UpdateButtonClick implements View.OnClickListener {
    @Override public void onClick(View v) {
      presenter.downloadData();
    }
  }
}
