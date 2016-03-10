/*
 *
 *  * Copyright (C) 2014 Antonio Leiva Gordillo.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.huaren.logistics.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.gc.materialdesign.views.CheckBox;
import com.huaren.logistics.R;
import com.huaren.logistics.main.MainActivity;
import com.huaren.logistics.util.CommonTool;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends Activity implements LoginView, View.OnClickListener {

  private MaterialEditText username;
  private MaterialEditText password;
  private LoginPresenter presenter;
  private CheckBox rememverCb;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    username = (MaterialEditText) findViewById(R.id.username);
    password = (MaterialEditText) findViewById(R.id.password);
    rememverCb = (CheckBox) findViewById(R.id.remember_cb);
    rememverCb.setOncheckListener(new RememberChecked());
    findViewById(R.id.btn_login).setOnClickListener(this);
    presenter = new LoginPresenter(this);
    presenter.initUsername();
  }

  @Override protected void onDestroy() {
    presenter.onDestroy();
    super.onDestroy();
  }

  @Override public void showProgress() {
    //progressBar.setVisibility(View.VISIBLE);
  }

  @Override public void hideProgress() {
    //progressBar.setVisibility(View.GONE);
  }

  @Override public void setUsernameError() {
    username.setError(getString(R.string.username_error));
  }

  @Override public void setPasswordError() {
    password.setError(getString(R.string.password_error));
  }

  @Override public void navigateToHome() {
    startActivity(new Intent(this, MainActivity.class));
    finish();
  }

  @Override public void fillRememberUserName(String userName) {
    username.setText(userName);
  }

  @Override public void setRememberCheck(final boolean isCheck) {
    rememverCb.post(new Runnable() {
      @Override public void run() {
        rememverCb.setChecked(isCheck);
      }
    });
  }

  @Override public void clearPasswordEt() {
    password.setText("");
  }

  @Override public void onClick(View v) {
    presenter.validateCredentials(username.getText().toString(), password.getText().toString());
  }

  private class RememberChecked implements CheckBox.OnCheckListener {
    @Override public void onCheck(CheckBox view, boolean check) {
      presenter.remmemberUserName(username.getText().toString(), check);
    }
  }
}
