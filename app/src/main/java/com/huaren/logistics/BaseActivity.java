package com.huaren.logistics;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.huaren.logistics.util.CommonTool;

public class BaseActivity extends Activity implements IBaseView {

  private TextView curUserTv;

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
      curUserTv = (TextView) findViewById(R.id.curr_user_tv);
      basePresent.initUserInfo();
    }
  }

  @Override public void setUserInfo(String curName) {
    curUserTv.setText(curName);
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
