package com.huaren.logistics.recycleinput;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gc.materialdesign.views.ButtonRectangle;
import com.huaren.logistics.ActivityCollector;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.R;
import com.huaren.logistics.bean.RecycleInput;
import com.rengwuxian.materialedittext.MaterialEditText;

public class RecycleInputDetailActivity extends BaseActivity
    implements IRecycleInputDetailInputView {

  private String customerId;
  private MaterialEditText inputEt;
  private ButtonRectangle inputBtn;
  private RecycleInputDetailPresent recycleInputDetailPresent;

  public static void actionStart(Context context, String customerId) {
    Intent intent = new Intent(context, RecycleInputDetailActivity.class);
    intent.putExtra("customerId", customerId);
    context.startActivity(intent);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recycle_input_detail);
    initUserInfo();
    inputEt = (MaterialEditText) findViewById(R.id.input_et);
    inputBtn = (ButtonRectangle) findViewById(R.id.input_btn);
    ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.activity_recycle_input_title);
    Intent intent = getIntent();
    customerId = intent.getStringExtra("customerId");
    recycleInputDetailPresent = new RecycleInputDetailPresent(this);
    inputBtn.setOnClickListener(new InputBtnClick());
  }

  @Override public void showDialog(String title, String message, final RecycleInput recycleInput) {
    MaterialDialog.Builder dialog =  new MaterialDialog.Builder(this).title(title)
        .content(message)
        .positiveText("确定")
        .onPositive(new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            recycleInputDetailPresent.updateRecycleInput(inputEt.getText().toString(), recycleInput);
          }
        })
        .negativeText("取消")
        .onNegative(new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            dialog.dismiss();
          }
        });
    dialog.show();
  }

  @Override public void enterRecycleInput() {
    startActivity(new Intent(this, RecycleInputActivity.class));
    ActivityCollector.removeActivity(this);
  }

  private class InputBtnClick implements View.OnClickListener {
    @Override public void onClick(View v) {
      recycleInputDetailPresent.recycleGoods(customerId, inputEt.getText().toString());
    }
  }
}
