package com.huaren.logistics.evaluation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.R;

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

  public static void actionStart(Context context, String customerId, String orderId) {
    Intent intent = new Intent(context, EvaluationOrderActivity.class);
    intent.putExtra("customerId", customerId);
    intent.putExtra("orderId", orderId);
    context.startActivity(intent);
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
