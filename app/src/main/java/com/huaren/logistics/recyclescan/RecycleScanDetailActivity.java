package com.huaren.logistics.recyclescan;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.view.MaterialListView;
import com.gc.materialdesign.views.ButtonRectangle;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.R;
import com.huaren.logistics.bean.SysDicValue;
import com.huaren.logistics.util.UiTool;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

public class RecycleScanDetailActivity extends BaseActivity implements IRecycleScanDetailView {

  private MaterialEditText scanEt;
  private ButtonRectangle scanBtn;
  private TextView recycleScanTv;
  private RecycleScanDetailPresent recycleScanDetailPresent;
  private MaterialListView mListView;

  private RadioGroup radioGroup;
  private List<SysDicValue> list;
  private String checkRadioName;
  private LinearLayout radioLl;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recycle_scan_detail);
    init();
  }

  private class ScanBtnClick implements View.OnClickListener {
    @Override public void onClick(View v) {
      if(list != null) {
        for (int i = 0; i < list.size(); i++) {
          SysDicValue sysDicValue = list.get(i);
          if (sysDicValue.getMyDisplayValue().equals(checkRadioName)) {
            recycleScanDetailPresent.recycleGoods(sysDicValue, scanEt.getText().toString());
          }
        }
      }
    }
  }

  @Override public void addCard(Card card) {
    mListView.add(card);
  }

  @Override public void init() {
    initUserInfo();
    scanEt = (MaterialEditText) findViewById(R.id.input_et);
    scanEt.setText("");
    scanBtn = (ButtonRectangle) findViewById(R.id.input_btn);
    recycleScanTv = (TextView) findViewById(R.id.recycle_scan_tv);
    radioLl = (LinearLayout) findViewById(R.id.radio_ll);
    mListView = (MaterialListView) findViewById(R.id.material_listview);
    mListView.clearAll();
    ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.activity_recycle_scan_title);
    this.list = null;
    if (radioGroup != null) {
      radioGroup.removeAllViews();
    }
    if (radioLl != null) {
      radioLl.removeAllViews();
    }
    recycleScanDetailPresent = new RecycleScanDetailPresent(this);
    recycleScanDetailPresent.initRecycleInputRadio();
    recycleScanDetailPresent.initRecycleScanList();
    scanBtn.setOnClickListener(new ScanBtnClick());
    scanEt.setOnKeyListener(onKey);
  }

  @Override
  protected void onResume() {
    UiTool.hideSoftInputMethod(RecycleScanDetailActivity.this, scanEt);
    super.onResume();
  }

  @Override
  public void initRadio(List<SysDicValue> list) {
    this.list = list;
    radioGroup = new RadioGroup(this);
    for (int i = 0; i < list.size(); i++) {
      SysDicValue sysDicValue = list.get(i);
      if(sysDicValue.getMyDisplayValue().equals("周转箱")){
        RadioButton radioButton = new RadioButton(this);
        radioButton.setText(sysDicValue.getMyDisplayValue());
        radioButton.setTextColor(getResources().getColor(R.color.blue_one));
        radioGroup.addView(radioButton);
        radioGroup.check(radioButton.getId());
        checkRadioName = radioButton.getText().toString();
      }
    }
    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
        selectRadioBtn();
      }
    });
    radioLl.addView(radioGroup);
  }

  @Override
  public void setRecycleNum(int size) {
    recycleScanTv.setText("" + size);
  }

  private void selectRadioBtn() {
    RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
    checkRadioName = radioButton.getText().toString();
  }

  View.OnKeyListener onKey = new View.OnKeyListener() {
    @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
      if (keyCode == KeyEvent.KEYCODE_ENTER) {
        switch (event.getAction()) {
          case KeyEvent.ACTION_UP:             //键盘松开
            if(list != null) {
              for (int i = 0; i < list.size(); i++) {
                SysDicValue sysDicValue = list.get(i);
                if (sysDicValue.getMyDisplayValue().equals(checkRadioName)) {
                  recycleScanDetailPresent.recycleGoods(sysDicValue, scanEt.getText().toString());
                }
              }
            }
            scanEt.requestFocus();
            break;
          case KeyEvent.ACTION_DOWN:          //键盘按下
            break;
        }
        return true;
      }
      return false;
    }
  };
}
