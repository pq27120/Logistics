package com.huaren.logistics.evaluation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.gc.materialdesign.views.ButtonRectangle;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.R;
import com.huaren.logistics.bean.SysDicValue;
import com.huaren.logistics.util.UiTool;
import com.rengwuxian.materialedittext.MaterialEditText;
import java.util.List;

public class EvaluationDetailActivity extends BaseActivity implements IEvaluationDetailView {

  private EvaluationDetailPresenter presenter;

  private String orderBatchId;

  private LinearLayout radioLl;

  private MaterialEditText passEt;

  private ButtonRectangle evBtn;

  private RadioGroup radioGroup;

  private List<SysDicValue> list;

  private String checkRadioName;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_evaluationdetail);
    initUserInfo();
    ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.evaluation);
    radioLl = (LinearLayout) findViewById(R.id.radio_ll);
    passEt = (MaterialEditText) findViewById(R.id.user_pass_et);
    UiTool.hideSoftInputMethod(EvaluationDetailActivity.this, passEt);
    evBtn = (ButtonRectangle) findViewById(R.id.evaluation_btn);
    Intent intent = getIntent();
    orderBatchId = intent.getStringExtra("orderBatchId");
    presenter = new EvaluationDetailPresenter(this);
    presenter.initEvaluationDetail(orderBatchId);
    evBtn.setOnClickListener(new EvaluationButtonClick());
  }

  public static void actionStart(Context context, String orderBatchId) {
    Intent intent = new Intent(context, EvaluationDetailActivity.class);
    intent.putExtra("orderBatchId", orderBatchId);
    context.startActivity(intent);
  }

  @Override public void initRadio(List<SysDicValue> list, boolean flag, String evaluation) {
    this.list = list;
    radioGroup = new RadioGroup(this);
    for (int i = 0; i < list.size(); i++) {
      SysDicValue sysDicValue = list.get(i);
      RadioButton radioButton = new RadioButton(this);
      if (!flag) {
        radioButton.setEnabled(false);
        evBtn.setEnabled(false);
      }
      if (!TextUtils.isEmpty(evaluation) && sysDicValue.getId().equals(Long.valueOf(evaluation))) {
        radioButton.setChecked(true);
      }
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

  @Override public void finishActivity() {
    finish();
  }

  private void selectRadioBtn() {
    RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
    checkRadioName = radioButton.getText().toString();
  }

  private class EvaluationButtonClick implements View.OnClickListener {
    @Override public void onClick(View v) {
      int radioButtonID = radioGroup.getCheckedRadioButtonId();
      if (radioButtonID == -1) {
        LogisticsApplication.getInstance().getSoundPoolUtil().playWrong();
        UiTool.showToast(EvaluationDetailActivity.this, "请选择评价选项！");
      } else {
        boolean passCheck = presenter.checkCustomerPass(orderBatchId, passEt.getText().toString());
        if (!passCheck) {
          LogisticsApplication.getInstance().getSoundPoolUtil().playWrong();
          UiTool.showToast(EvaluationDetailActivity.this, "客户密码不正确！");
        } else {
          for (int i = 0; i < list.size(); i++) {
            SysDicValue sysDicValue = list.get(i);
            if (sysDicValue.getMyDisplayValue().equals(checkRadioName)) {
              presenter.evaluationOrder(orderBatchId, sysDicValue);
            }
          }
        }
      }
    }
  }
}
