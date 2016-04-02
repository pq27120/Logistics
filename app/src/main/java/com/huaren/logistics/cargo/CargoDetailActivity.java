package com.huaren.logistics.cargo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.view.MaterialListView;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.R;

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
}