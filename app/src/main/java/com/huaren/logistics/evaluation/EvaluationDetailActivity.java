package com.huaren.logistics.evaluation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.R;

public class EvaluationDetailActivity extends BaseActivity implements IEvaluationDetailView{

  private EvaluationDetailPresenter presenter;

  private String orderId;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_evaluationdetail);
    initUserInfo();
    ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.evaluation);
    Intent intent = getIntent();
    orderId = intent.getStringExtra("orderId");
    presenter = new EvaluationDetailPresenter(this);
    presenter.initEvaluationDetail(orderId);
  }
}
