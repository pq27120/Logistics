package com.huaren.logistics.detail;

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

  private TextView nameTv;
  private TextView driverTv;
  private TextView licensePlateTv;
  private TextView unloadTv;
  private TextView loadTv;

  private MaterialEditText loadEt;
  private ButtonRectangle loadBtn;

  private MaterialListView mListView;

  private long customer;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cargo_detail);
    nameTv = (TextView) findViewById(R.id.shipper_tv);
    driverTv = (TextView) findViewById(R.id.driver_tv);
    licensePlateTv = (TextView) findViewById(R.id.license_plate_tv);
    unloadTv = (TextView) findViewById(R.id.unload_tv);
    loadTv = (TextView) findViewById(R.id.load_tv);
    loadEt = (MaterialEditText) findViewById(R.id.load_et);
    loadBtn = (ButtonRectangle) findViewById(R.id.load_btn);
    mListView = (MaterialListView) findViewById(R.id.material_listview);
    ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.cargo);
    Intent intent = getIntent();
    customer = intent.getLongExtra("customerId", 0);
    presenter = new CargoDetailPresenter(this);
    loadBtn.setOnClickListener(new LoadButtonClick());
    presenter.initUserInfo();
    presenter.initCargoDetail(customer);
  }

  @Override public void setUserInfo(String name, String driver, String licensePlate) {
    nameTv.setText(name);
    driverTv.setText(driver);
    licensePlateTv.setText(licensePlate);
  }

  @Override public void addCard(Card card) {
    mListView.add(card);
  }

  @Override public void setLoadInfo(int loadGount, int unLoadCount) {
    unloadTv.setText(unLoadCount + "");
    loadTv.setText(loadGount + "");
  }

  @Override public void showLoadDialog(String title, String message) {
    MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
        .title(title)
        .content(message)
        .positiveText("确定")
        .onPositive(new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            presenter.updateLoadCargo(customer, loadEt.getText().toString());
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
    presenter.initUserInfo();
    mListView.clearAll();
    presenter.initCargoDetail(customer);
  }

  @Override public String getUnLoadCount() {
    return unloadTv.getText().toString();
  }

  private class LoadButtonClick implements View.OnClickListener {
    @Override public void onClick(View v) {
      presenter.loadCargo(customer, loadEt.getText().toString());
    }
  }
}
