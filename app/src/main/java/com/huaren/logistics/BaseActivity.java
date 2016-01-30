package com.huaren.logistics;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.huaren.logistics.util.CommonTool;

public class BaseActivity extends Activity implements IBaseView {

  private TextView nameTv;
  private TextView driverTv;
  private TextView licensePlateTv;

  private BasePresent basePresent;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    CommonTool.showLog(getClass().getSimpleName());
    ActivityCollector.addActivity(this);
    basePresent = new BasePresent(this);
  }

  public void backClick(View view) {
    finish();
  }

  protected void initUserInfo() {
    if(isUserShow()) {
      nameTv = (TextView) findViewById(R.id.shipper_tv);
      driverTv = (TextView) findViewById(R.id.driver_tv);
      licensePlateTv = (TextView) findViewById(R.id.license_plate_tv);
      basePresent.initUserInfo();
    }
  }

  @Override public void setUserInfo(String name, String driver, String licensePlate) {
    nameTv.setText(name);
    driverTv.setText(driver);
    licensePlateTv.setText(licensePlate);
  }

  /** 是否展示用户信息 */
  public boolean isUserShow() {
    return true;
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    ActivityCollector.removeActivity(this);
  }
}
