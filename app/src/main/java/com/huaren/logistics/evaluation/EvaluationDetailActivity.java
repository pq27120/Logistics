package com.huaren.logistics.evaluation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.R;

public class EvaluationDetailActivity extends BaseActivity implements IEvaluationDetailView{

  private EvaluationDetailPresenter presenter;

  private TextView nameTv;
  private TextView driverTv;
  private TextView licensePlateTv;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_evaluationdetail);
    nameTv = (TextView) findViewById(R.id.shipper_tv);
    driverTv = (TextView) findViewById(R.id.driver_tv);
    licensePlateTv = (TextView) findViewById(R.id.license_plate_tv);
    ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.evaluation);
    presenter = new EvaluationDetailPresenter(this);
    presenter.initUserInfo();
    presenter.initEvaluationDetail();
  }

  @Override public void setUserInfo(String name, String driver, String licensePlate) {
    nameTv.setText(name);
    driverTv.setText(driver);
    licensePlateTv.setText(licensePlate);
  }
}
