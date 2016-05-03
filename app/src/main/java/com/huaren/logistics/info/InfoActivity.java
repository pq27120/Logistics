package com.huaren.logistics.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.gc.materialdesign.views.ButtonRectangle;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.R;
import com.huaren.logistics.login.LoginActivity;
import com.huaren.logistics.util.CommonTool;
import com.kyleduo.switchbutton.SwitchButton;

public class InfoActivity extends BaseActivity implements InfoView {
  private InfoPresenter presenter;

  private ButtonRectangle quitBtn;
  private SwitchButton switchButton;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_info);
    initUserInfo();
    ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.info);
    quitBtn = (ButtonRectangle) findViewById(R.id.btn_quit);
    switchButton = (SwitchButton)findViewById(R.id.hide_keyboard_switch);
    presenter = new InfoPresenter(this);
    quitBtn.setOnClickListener(new QuitButtonClick());
    presenter.initSwitch();
    switchButton.setOnCheckedChangeListener(new SwicthClick());
  }

  @Override public void enterLogin() {
    startActivity(new Intent(this, LoginActivity.class));
    finish();
  }

  @Override
  public void setSwitchCheck(boolean checkState) {
    switchButton.setChecked(checkState);
  }

  private class QuitButtonClick implements View.OnClickListener {
    @Override public void onClick(View v) {
      presenter.clearUserInfo();
    }
  }

  private class SwicthClick implements SwitchButton.OnCheckedChangeListener {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
      if(isChecked){
        CommonTool.setSharePreference(InfoActivity.this, "hideKeyBoard", "1");
      }else{
        CommonTool.setSharePreference(InfoActivity.this, "hideKeyBoard", "0");
      }
    }
  }
}
