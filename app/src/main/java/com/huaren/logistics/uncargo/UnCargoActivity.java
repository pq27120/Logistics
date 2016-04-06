package com.huaren.logistics.uncargo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.R;

public class UnCargoActivity extends BaseActivity implements IUnCargoView {
  private MaterialListView mListView;

  private UnCargoPresenter presenter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_uncargo);
    initUserInfo();
    mListView = (MaterialListView) findViewById(R.id.material_listview);
    mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
      @Override public void onItemClick(Card card, int position) {
        Log.d("CARD_TYPE", card.getTag().toString());
        Intent intent = new Intent(UnCargoActivity.this, UnCargoOrderActivity.class);
        intent.putExtra("customerId", card.getTag().toString());
        startActivity(intent);
      }

      @Override public void onItemLongClick(Card card, int position) {
        Log.d("LONG_CLICK", card.getTag().toString());
      }
    });
    ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.uncargo);
    presenter = new UnCargoPresenter(this);
    presenter.initCargoList();
  }

  @Override public void addCard(Card card) {
    mListView.add(card);
  }
}
