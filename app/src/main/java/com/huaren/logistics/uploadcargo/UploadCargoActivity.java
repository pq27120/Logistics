package com.huaren.logistics.uploadcargo;

import android.app.Dialog;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.BasicButtonsCardProvider;
import com.dexafree.materialList.view.MaterialListView;
import com.gc.materialdesign.views.ButtonRectangle;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.R;
import com.huaren.logistics.customer.CustomProgress;

public class UploadCargoActivity extends BaseActivity implements IUploadCargoView {

  private ButtonRectangle uploadButton;

  private UploadCargoPresenter presenter;

  private MaterialListView mListView;

  private Dialog dialog;    //进度条对话框

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_uploadcargo);
    mListView = (MaterialListView) findViewById(R.id.upload_cargo_listview);
    uploadButton = (ButtonRectangle) findViewById(R.id.btn_upload);
    ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.upload_cargo);
    presenter = new UploadCargoPresenter(this);
    dialog = CustomProgress.show(this, "上传中...", false, null);
    presenter.uploadOrderData();
    uploadButton.setOnClickListener(new UpdateButtonClick());
  }

  /**
   * 用Handler来更新UI
   */
  private Handler handler = new Handler(){
    @Override
    public void handleMessage(Message msg) {
      //关闭ProgressDialog
      dialog.dismiss();
      mListView.setVisibility(View.VISIBLE);
      uploadButton.setVisibility(View.VISIBLE);
    }};

  @Override public void showProgress() {

  }

  @Override public void hideProgress() {

  }

  @Override public void hideUpdateView() {
  }

  @Override public void showUpdateView(String time, String info) {
    mListView.clearAll();
    Card card = new Card.Builder(this)
        .withProvider(BasicButtonsCardProvider.class)
        .setTitle("上传成功")
        .setDescription("上传时间：" + time  + info)
        .endConfig().build();
    mListView.add(card);
    handler.sendEmptyMessage(0);
    mListView.setVisibility(View.VISIBLE);
    uploadButton.setVisibility(View.VISIBLE);
  }

  @Override public AssetManager getAssetManager() {
    return getAssets();
  }

  @Override
  public void finishActivity() {
    dialog.dismiss();
    finish();
  }

  private class UpdateButtonClick implements View.OnClickListener {
    @Override public void onClick(View v) {
      mListView.setVisibility(View.GONE);
      uploadButton.setVisibility(View.GONE);
      dialog = CustomProgress.show(UploadCargoActivity.this, "上传中...", false, null);
      presenter.uploadOrderData();
    }
  }
}
