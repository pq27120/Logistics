package com.huaren.logistics.cargo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.OnButtonClickListener;
import com.dexafree.materialList.card.provider.BasicImageButtonsCardProvider;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.R;
import com.huaren.logistics.detail.CargoDetailActivity;

public class CargoActivity extends BaseActivity implements ICargoView {

  private MaterialListView mListView;

  private CargoPresenter presenter;

  private TextView nameTv;
  private TextView driverTv;
  private TextView licensePlateTv;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cargo);
    nameTv = (TextView) findViewById(R.id.shipper_tv);
    driverTv = (TextView) findViewById(R.id.driver_tv);
    licensePlateTv = (TextView) findViewById(R.id.license_plate_tv);
    mListView = (MaterialListView) findViewById(R.id.material_listview);
    mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
      @Override public void onItemClick(Card card, int position) {
        Log.d("CARD_TYPE", card.getTag().toString());
        Intent intent = new Intent(CargoActivity.this, CargoDetailActivity.class);
        intent.putExtra("customerId", Long.valueOf(card.getTag().toString()));
        startActivity(intent);
      }

      @Override public void onItemLongClick(Card card, int position) {
        Log.d("LONG_CLICK", card.getTag().toString());
      }
    });
    ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.cargo);
    presenter = new CargoPresenter(this);
    presenter.initUserInfo();
    presenter.initCargoList();
  }

  @Override public void setUserInfo(String name, String driver, String licensePlate) {
    nameTv.setText(name);
    driverTv.setText(driver);
    licensePlateTv.setText(licensePlate);
  }

  @Override public void addCard(Card card) {
    mListView.add(card);
  }
}
