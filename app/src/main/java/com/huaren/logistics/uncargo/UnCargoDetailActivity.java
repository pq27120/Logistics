package com.huaren.logistics.uncargo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.view.MaterialListView;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.R;

public class UnCargoDetailActivity extends BaseActivity implements IUnCargoDetailView{

  private UnCargoDetailPresenter presenter;

  private MaterialListView mListView;

  private String orderId;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_uncargo_detail);
    initUserInfo();
    mListView = (MaterialListView) findViewById(R.id.material_listview);
    ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.uncargo);
    Intent intent = getIntent();
    orderId = intent.getStringExtra("orderId");
    presenter = new UnCargoDetailPresenter(this);
    presenter.initCargoDetail(orderId);
  }

  @Override public void addCard(Card card) {
    mListView.add(card);
  }
}
