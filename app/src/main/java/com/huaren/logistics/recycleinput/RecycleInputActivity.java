package com.huaren.logistics.recycleinput;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.R;
import com.huaren.logistics.evaluation.EvaluationDetailActivity;

public class RecycleInputActivity extends BaseActivity implements IRecycleInputView {
  private MaterialListView mListView;

  private RecycleInputPresenter presenter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recycle_input);
    initUserInfo();
    mListView = (MaterialListView) findViewById(R.id.material_listview);
    mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
      @Override public void onItemClick(Card card, int position) {
        Log.d("CARD_TYPE", card.getTag().toString());
        Intent intent = new Intent(RecycleInputActivity.this, RecycleInputDetailActivity.class);
        intent.putExtra("orderBatchId", card.getTag().toString());
        startActivity(intent);
        finish();
      }

      @Override public void onItemLongClick(Card card, int position) {
        Log.d("LONG_CLICK", card.getTag().toString());
      }
    });
    ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.activity_recycle_input_title);
    presenter = new RecycleInputPresenter(this);
    presenter.initCargoList();
  }

  @Override public void addCard(Card card) {
    mListView.add(card);
  }
}
