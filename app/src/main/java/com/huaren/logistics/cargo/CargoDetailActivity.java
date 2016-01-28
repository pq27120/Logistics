package com.huaren.logistics.cargo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.view.MaterialListView;
import com.gc.materialdesign.views.ButtonRectangle;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.R;
import com.rengwuxian.materialedittext.MaterialEditText;

public class CargoDetailActivity extends BaseActivity implements ICargoDetailView {

  private CargoDetailPresenter presenter;

  private MaterialListView mListView;

  private String orderId;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cargo_detail);
    initUserInfo();
    mListView = (MaterialListView) findViewById(R.id.material_listview);
    ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.cargo);
    Intent intent = getIntent();
    orderId = intent.getStringExtra("orderId");
    presenter = new CargoDetailPresenter(this);
    presenter.initCargoDetail(orderId);
  }

  @Override public void addCard(Card card) {
    mListView.add(card);
  }

  @Override public void showLoadDialog(String title, String message) {
    MaterialDialog.Builder builder = new MaterialDialog.Builder(this).title(title)
        .content(message)
        .positiveText("确定")
        .onPositive(new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            //presenter.updateLoadCargo(orderId, loadEt.getText().toString());
          }
        }).negativeText("取消").onNegative(new MaterialDialog.SingleButtonCallback() {
          @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            dialog.dismiss();
          }
        });

    MaterialDialog dialog = builder.build();
    dialog.show();
  }

  @Override public void reInit() {
    initUserInfo();
    mListView.clearAll();
    presenter.initCargoDetail(orderId);
  }
}