package com.huaren.logistics.recyclescan;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.view.MaterialListView;
import com.gc.materialdesign.views.ButtonRectangle;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.R;
import com.huaren.logistics.util.UiTool;
import com.rengwuxian.materialedittext.MaterialEditText;

public class RecycleScanDetailActivity extends BaseActivity implements IRecycleScanDetailView {

  private MaterialEditText scanEt;
  private ButtonRectangle scanBtn;
  private RecycleScanDetailPresent recycleScanDetailPresent;
  private MaterialListView mListView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recycle_scan_detail);
    init();
  }

  private class ScanBtnClick implements View.OnClickListener {
    @Override public void onClick(View v) {
      recycleScanDetailPresent.recycleGoods(scanEt.getText().toString());
    }
  }

  @Override public void addCard(Card card) {
    mListView.add(card);
  }

  @Override public void init() {
    initUserInfo();
    scanEt = (MaterialEditText) findViewById(R.id.input_et);
    UiTool.hideSoftInputMethod(RecycleScanDetailActivity.this, scanEt);
    scanBtn = (ButtonRectangle) findViewById(R.id.input_btn);
    mListView = (MaterialListView) findViewById(R.id.material_listview);
    mListView.clearAll();
    ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.activity_recycle_scan_title);
    recycleScanDetailPresent = new RecycleScanDetailPresent(this);
    recycleScanDetailPresent.initRecycleScanList();
    scanBtn.setOnClickListener(new ScanBtnClick());
  }
}
