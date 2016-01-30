package com.huaren.logistics.recyclescan;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.R;

public class RecycleScanActivity extends BaseActivity implements IRecycleScanView {
  private MaterialListView mListView;

  private RecycleScanPresent presenter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recycle_scan);
    initUserInfo();
    mListView = (MaterialListView) findViewById(R.id.material_listview);
    mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
      @Override public void onItemClick(Card card, int position) {
        Log.d("CARD_TYPE", card.getTag().toString());
        RecycleScanDetailActivity.actionStart(RecycleScanActivity.this, card.getTag().toString());
      }

      @Override public void onItemLongClick(Card card, int position) {
        Log.d("LONG_CLICK", card.getTag().toString());
      }
    });
    ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.activity_recycle_input_title);
    presenter = new RecycleScanPresent(this);
    initUserInfo();
    presenter.initCustomList();
  }

  @Override public void addCard(Card card) {
    mListView.add(card);
  }
}
