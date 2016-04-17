package com.huaren.logistics.uploadcargo;

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

public class UploadCargoActivity extends BaseActivity implements IUploadCargoView {

  private ButtonRectangle uploadButton;

  private UploadCargoPresenter presenter;

  private MaterialListView mListView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_uploadcargo);
    mListView = (MaterialListView) findViewById(R.id.upload_cargo_listview);
    uploadButton = (ButtonRectangle) findViewById(R.id.btn_upload);
    ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.upload_cargo);
    presenter = new UploadCargoPresenter(this);
    presenter.uploadOrderData();
    presenter.uploadEvaluationData();
    uploadButton.setOnClickListener(new UpdateButtonClick());
  }

  @Override public void showProgress() {

  }

  @Override public void hideProgress() {

  }

  @Override public void hideUpdateView() {
  }

  @Override public void showUpdateView(String time, String info) {
    Card card = new Card.Builder(this)
        .withProvider(BasicButtonsCardProvider.class)
        .setTitle("上传成功")
        .setDescription("上传时间：" + time + "," + info)
        .endConfig().build();
    mListView.add(card);
  }

  @Override public AssetManager getAssetManager() {
    return getAssets();
  }

  private class UpdateButtonClick implements View.OnClickListener {
    @Override public void onClick(View v) {
      presenter.uploadOrderData();
      presenter.uploadEvaluationData();
    }
  }
}
