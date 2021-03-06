package com.huaren.logistics.cargo;

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

public class CargoActivity extends BaseActivity implements ICargoView {

  private MaterialListView mListView;

  private CargoPresenter presenter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cargo);
    initUserInfo();
    mListView = (MaterialListView) findViewById(R.id.material_listview);
    mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
      @Override public void onItemClick(Card card, int position) {
        Log.d("CARD_TYPE", card.getTag().toString());
        CargoOrderActivity.actionStart(CargoActivity.this, card.getTag().toString());
        finish();
      }

      @Override public void onItemLongClick(Card card, int position) {
        Log.d("LONG_CLICK", card.getTag().toString());
      }
    });
    ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.cargo);
    presenter = new CargoPresenter(this);
    initUserInfo();
    presenter.initCargoList();
  }

  public static void actionStart(Context context) {
    Intent intent = new Intent(context, CargoActivity.class);
    context.startActivity(intent);
  }

  @Override public void addCard(Card card) {
    mListView.add(card);
  }

  @Override
  public void addStartCard(Card card) {
    mListView.addAtStart(card);
  }
}
