package com.huaren.logistics.cargo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.gc.materialdesign.views.ButtonRectangle;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.R;
import com.huaren.logistics.util.UiTool;
import com.rengwuxian.materialedittext.MaterialEditText;

public class CargoOrderActivity extends BaseActivity implements ICargoOrderView {
  private CargoOrderPresenter presenter;

  private MaterialEditText loadEt;
  private ButtonRectangle loadBtn;

  private MaterialListView mListView;

  private String customerId;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cargo_order);
    initUserInfo();
    loadEt = (MaterialEditText) findViewById(R.id.load_et);
    UiTool.hideSoftInputMethod(CargoOrderActivity.this, loadEt);
    loadBtn = (ButtonRectangle) findViewById(R.id.load_btn);
    mListView = (MaterialListView) findViewById(R.id.material_listview);
    ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.cargo);
    Intent intent = getIntent();
    customerId = intent.getStringExtra("customerId");
    presenter = new CargoOrderPresenter(this);
    loadBtn.setOnClickListener(new LoadButtonClick());
    presenter.initCargoOrder(customerId);
  }

  public static void actionStart(Context context, String customerId) {
    Intent intent = new Intent(context, CargoOrderActivity.class);
    intent.putExtra("customerId", customerId);
    context.startActivity(intent);
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
            presenter.updateOrderCargo(customerId, loadEt.getText().toString());
          }
        })
        .negativeText("取消")
        .onNegative(new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            dialog.dismiss();
          }
        });

    MaterialDialog dialog = builder.build();
    dialog.show();
  }

  @Override public void reInit() {
    initUserInfo();
    mListView.clearAll();
    presenter.initCargoOrder(customerId);
  }

  private class LoadButtonClick implements View.OnClickListener {
    @Override public void onClick(View v) {
      presenter.loadDetailCargo(customerId, loadEt.getText().toString());
    }
  }
}
