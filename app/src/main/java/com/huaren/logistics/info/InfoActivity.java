package com.huaren.logistics.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.gc.materialdesign.views.ButtonRectangle;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.R;
import com.huaren.logistics.login.LoginActivity;

public class InfoActivity extends BaseActivity implements InfoView {
  private InfoPresenter presenter;

  private ButtonRectangle quitBtn;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_info);
    initUserInfo();
    ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.info);
    quitBtn = (ButtonRectangle) findViewById(R.id.btn_quit);
    presenter = new InfoPresenter(this);
    quitBtn.setOnClickListener(new QuitButtonClick());
  }

  @Override public void enterLogin() {
    startActivity(new Intent(this, LoginActivity.class));
    finish();
  }

  private class QuitButtonClick implements View.OnClickListener {
    @Override public void onClick(View v) {
      presenter.clearUserInfo();
    }
  }
}
