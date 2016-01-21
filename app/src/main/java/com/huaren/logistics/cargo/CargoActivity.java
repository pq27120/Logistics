package com.huaren.logistics.cargo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.OnButtonClickListener;
import com.dexafree.materialList.card.provider.BasicImageButtonsCardProvider;
import com.dexafree.materialList.view.MaterialListView;
import com.huaren.logistics.R;

public class CargoActivity extends Activity implements ICargoView{

  private MaterialListView mListView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cargo);
    mListView = (MaterialListView) findViewById(R.id.material_listview);
    Card card = new Card.Builder(this)
        .withProvider(BasicImageButtonsCardProvider.class)
        .setTitle("Card number 4")
        .setDescription("Lorem ipsum dolor sit amet")
        //.setLeftButtonText("Izquierda")
        //.setRightButtonText("Derecha")
        .setOnLeftButtonClickListener(new OnButtonClickListener() {
          @Override public void onButtonClicked(View view, Card card) {

          }
        })
        .endConfig()
        .build();
    mListView.add(card);
  }
}
