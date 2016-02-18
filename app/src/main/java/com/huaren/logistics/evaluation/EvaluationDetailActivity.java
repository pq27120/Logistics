package com.huaren.logistics.evaluation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.R;
import com.huaren.logistics.bean.SysDicValue;
import java.util.List;

public class EvaluationDetailActivity extends BaseActivity implements IEvaluationDetailView{

  private EvaluationDetailPresenter presenter;

  private String orderId;

  private LinearLayout radioLl;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_evaluationdetail);
    initUserInfo();
    ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.evaluation);
    radioLl = (LinearLayout)findViewById(R.id.radio_ll);
    Intent intent = getIntent();
    orderId = intent.getStringExtra("orderId");
    presenter = new EvaluationDetailPresenter(this);
    presenter.initEvaluationDetail(orderId);
  }

  @Override public void initRadio(List<SysDicValue> list) {
    RadioGroup radioGroup = new RadioGroup(this);
    for (int i = 0; i < list.size(); i++) {
      SysDicValue sysDicValue = list.get(i);
      RadioButton radioButton = new RadioButton(this);
      radioButton.setText(sysDicValue.getMyDisplayValue());
      radioButton.setTextColor(getResources().getColor(R.color.blue_one));
      radioGroup.addView(radioButton);
    }
    radioLl.addView(radioGroup);
  }
}
