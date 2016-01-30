package com.huaren.logistics.recyclescan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.gc.materialdesign.views.ButtonRectangle;
import com.huaren.logistics.ActivityCollector;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.R;
import com.rengwuxian.materialedittext.MaterialEditText;

public class RecycleScanDetailActivity extends BaseActivity implements IRecycleScanDetailView{

  private String customerId;

  private MaterialEditText scanEt;
  private ButtonRectangle scanBtn;
  private RecycleScanDetailPresent recycleScanDetailPresent;

  public static void actionStart(Context context, String customerId) {
    Intent intent = new Intent(context, RecycleScanDetailActivity.class);
    intent.putExtra("customerId", customerId);
    context.startActivity(intent);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recycle_input_detail);
    initUserInfo();
    scanEt = (MaterialEditText) findViewById(R.id.input_et);
    scanBtn = (ButtonRectangle) findViewById(R.id.input_btn);
    ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.activity_recycle_input_title);
    Intent intent = getIntent();
    customerId = intent.getStringExtra("customerId");
    recycleScanDetailPresent = new RecycleScanDetailPresent(this);
    scanBtn.setOnClickListener(new ScanBtnClick());
  }

  @Override public void enterRecycleScan() {
    startActivity(new Intent(this, RecycleScanActivity.class));
    ActivityCollector.removeActivity(this);
  }

  private class ScanBtnClick implements View.OnClickListener {
    @Override public void onClick(View v) {
      recycleScanDetailPresent.recycleGoods(customerId, scanEt.getText().toString());
    }
  }
}
