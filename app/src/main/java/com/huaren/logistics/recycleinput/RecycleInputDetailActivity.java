package com.huaren.logistics.recycleinput;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.view.MaterialListView;
import com.gc.materialdesign.views.ButtonRectangle;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.R;
import com.huaren.logistics.bean.RecycleInput;
import com.huaren.logistics.util.UiTool;
import com.rengwuxian.materialedittext.MaterialEditText;

public class RecycleInputDetailActivity extends BaseActivity
    implements IRecycleInputDetailInputView {

  private MaterialEditText inputEt;
  private ButtonRectangle inputBtn;
  private RecycleInputDetailPresent recycleInputDetailPresent;
  private MaterialListView mListView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recycle_input_detail);
    init();
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

  @Override public void addCard(Card card) {
    mListView.add(card);
  }

  @Override public void init() {
    initUserInfo();
    inputEt = (MaterialEditText) findViewById(R.id.input_et);
    UiTool.hideSoftInputMethod(RecycleInputDetailActivity.this, inputEt);
    inputBtn = (ButtonRectangle) findViewById(R.id.input_btn);
    mListView = (MaterialListView) findViewById(R.id.material_listview);
    mListView.clearAll();
    ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.activity_recycle_input_title);
    recycleInputDetailPresent = new RecycleInputDetailPresent(this);
    recycleInputDetailPresent.initRecycleInputList();
    inputBtn.setOnClickListener(new InputBtnClick());
  }

  private class InputBtnClick implements View.OnClickListener {
    @Override public void onClick(View v) {
      recycleInputDetailPresent.recycleGoods(inputEt.getText().toString());
    }
  }
}
