package com.huaren.logistics.evaluation;

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

public class EvaluationOrderActivity extends BaseActivity implements IEvaluationOrderView {
  private EvaluationOrderPresenter presenter;

  private MaterialListView mListView;

  private String customerId;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_evaluation_order);
    initUserInfo();
    mListView = (MaterialListView) findViewById(R.id.material_listview);
    mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
      @Override public void onItemClick(Card card, int position) {
        Log.d("CARD_TYPE", card.getTag().toString());
        Intent intent = new Intent(EvaluationOrderActivity.this, EvaluationDetailActivity.class);
        intent.putExtra("orderId", card.getTag().toString());
        intent.putExtra("customerId", customerId);
        startActivity(intent);
      }

      @Override public void onItemLongClick(Card card, int position) {
        Log.d("LONG_CLICK", card.getTag().toString());
      }
    });
    ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.evaluation);
    Intent intent = getIntent();
    customerId = intent.getStringExtra("customerId");
    presenter = new EvaluationOrderPresenter(this);
    presenter.initCargoOrder(customerId);
  }

  @Override public void addCard(Card card) {
    mListView.add(card);
  }

  @Override public void reInit() {
    initUserInfo();
    mListView.clearAll();
    presenter.initCargoOrder(customerId);
  }
}
