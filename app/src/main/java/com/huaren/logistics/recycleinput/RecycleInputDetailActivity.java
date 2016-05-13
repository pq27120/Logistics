package com.huaren.logistics.recycleinput;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.view.MaterialListView;
import com.gc.materialdesign.views.ButtonRectangle;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.R;
import com.huaren.logistics.bean.RecycleInput;
import com.huaren.logistics.bean.SysDicValue;
import com.huaren.logistics.util.UiTool;
import com.rengwuxian.materialedittext.MaterialEditText;
import java.util.List;

public class RecycleInputDetailActivity extends BaseActivity
    implements IRecycleInputDetailInputView {

  private MaterialEditText inputEt;
  private ButtonRectangle inputBtn;
  private RecycleInputDetailPresent recycleInputDetailPresent;
  private MaterialListView mListView;
  private String orderBatchId;

  private RadioGroup radioGroup;
  private List<SysDicValue> list;
  private String checkRadioName;
  private LinearLayout radioLl;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recycle_input_detail);
    init();
  }

  public static void actionStart(Context context, String orderBatchId) {
    Intent intent = new Intent(context, RecycleInputDetailActivity.class);
    intent.putExtra("orderBatchId", orderBatchId);
    context.startActivity(intent);
  }

  @Override
  public void backClick(View view) {
    RecycleInputActivity.actionStart(RecycleInputDetailActivity.this);
    super.backClick(view);
  }

  @Override public void showDialog(String title, String message, final RecycleInput recycleInput) {
    MaterialDialog.Builder dialog = new MaterialDialog.Builder(this).title(title)
        .content(message)
        .positiveText("确定")
        .onPositive(new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            for (int i = 0; i < list.size(); i++) {
              SysDicValue sysDicValue = list.get(i);
              if (sysDicValue.getMyDisplayValue().equals(checkRadioName)) {
                recycleInputDetailPresent.updateRecycleInput(sysDicValue,
                    inputEt.getText().toString(), recycleInput);
              }
            }
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
    inputEt.setText("");
    inputBtn = (ButtonRectangle) findViewById(R.id.input_btn);
    radioLl = (LinearLayout) findViewById(R.id.radio_ll);
    mListView = (MaterialListView) findViewById(R.id.material_listview);
    mListView.clearAll();
    ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.activity_recycle_input_title);
    Intent intent = getIntent();
    orderBatchId = intent.getStringExtra("orderBatchId");
    recycleInputDetailPresent = new RecycleInputDetailPresent(this);
    this.list = null;
    if (radioGroup != null) {
      radioGroup.removeAllViews();
    }
    if (radioLl != null) {
      radioLl.removeAllViews();
    }
    recycleInputDetailPresent.initRecycleInputRadio();
    recycleInputDetailPresent.initRecycleInputList(orderBatchId);
    inputBtn.setOnClickListener(new InputBtnClick());
  }

  @Override
  protected void onResume() {
    UiTool.hideSoftInputMethod(RecycleInputDetailActivity.this, inputEt);
    super.onResume();
  }

  @Override public void initRadio(List<SysDicValue> list) {
    this.list = list;
    radioGroup = new RadioGroup(this);
    for (int i = 0; i < list.size(); i++) {
      SysDicValue sysDicValue = list.get(i);
      RadioButton radioButton = new RadioButton(this);
      radioButton.setText(sysDicValue.getMyDisplayValue());
      radioButton.setTextColor(getResources().getColor(R.color.blue_one));
      radioGroup.addView(radioButton);
    }
    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
        selectRadioBtn();
      }
    });
    radioLl.addView(radioGroup);
  }

  private void selectRadioBtn() {
    RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
    checkRadioName = radioButton.getText().toString();
  }

  private class InputBtnClick implements View.OnClickListener {
    @Override public void onClick(View v) {
      for (int i = 0; i < list.size(); i++) {
        SysDicValue sysDicValue = list.get(i);
        if (sysDicValue.getMyDisplayValue().equals(checkRadioName)) {
          recycleInputDetailPresent.recycleGoods(orderBatchId, sysDicValue,
              inputEt.getText().toString());
        }
      }
    }
  }
}
