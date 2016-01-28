package com.huaren.logistics.uncargo;

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
import com.huaren.logistics.cargo.CargoDetailActivity;
import com.rengwuxian.materialedittext.MaterialEditText;

public class UnCargoOrderActivity extends BaseActivity implements IUnCargoOrderView {
  private UnCargoOrderPresenter presenter;

  private MaterialEditText unRemoveEt;
  private ButtonRectangle unRemoveBtn;

  private MaterialListView mListView;

  private String customerId;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_uncargo_order);
    initUserInfo();
    unRemoveEt = (MaterialEditText) findViewById(R.id.load_et);
    unRemoveBtn = (ButtonRectangle) findViewById(R.id.remove_btn);
    mListView = (MaterialListView) findViewById(R.id.material_listview);
    mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
      @Override public void onItemClick(Card card, int position) {
        Log.d("CARD_TYPE", card.getTag().toString());
        Intent intent = new Intent(UnCargoOrderActivity.this, UnCargoDetailActivity.class);
        intent.putExtra("orderId", card.getTag().toString());
        startActivity(intent);
      }

      @Override public void onItemLongClick(Card card, int position) {
        Log.d("LONG_CLICK", card.getTag().toString());
      }
    });
    ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.cargo);
    Intent intent = getIntent();
    customerId = intent.getStringExtra("customerId");
    presenter = new UnCargoOrderPresenter(this);
    unRemoveBtn.setOnClickListener(new LoadButtonClick());
    presenter.initCargoOrder(customerId);
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
            presenter.updateOrderCargo(unRemoveEt.getText().toString());
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
      presenter.loadDetailCargo(customerId, unRemoveEt.getText().toString());
    }
  }
}
