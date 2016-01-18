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

import android.content.Context;
import com.huaren.logistics.entity.UserInfo;
import com.huaren.logistics.util.CommonTool;
import com.huaren.logistics.util.StringTool;

public class LoginPresenter implements OnLoginFinishedListener {

  private LoginView loginView;
  private LoginInteractor loginInteractor;

  public LoginPresenter(LoginView loginView) {
    this.loginView = loginView;
    this.loginInteractor = new LoginInteractorImpl();
  }

  public void validateCredentials(String username, String password) {
    if (loginView != null) {
      loginView.showProgress();
    }
    loginInteractor.login(username, password, this);
  }

  public void remmemberUserName(String userName, boolean isCheck) {
    if (isCheck) {
      CommonTool.setSharePreference((Context) loginView, "userName", userName);
      CommonTool.setSharePreference((Context) loginView, "rememberUser", "true");
    } else {
      CommonTool.setSharePreference((Context) loginView, "userName", "");
      CommonTool.setSharePreference((Context) loginView, "rememberUser", "false");
    }
  }

  public void onDestroy() {
    loginView = null;
  }

  @Override public void onUsernameError() {
    if (loginView != null) {
      loginView.setUsernameError();
      loginView.hideProgress();
    }
  }

  @Override public void onPasswordError() {
    if (loginView != null) {
      loginView.setPasswordError();
      loginView.hideProgress();
    }
  }

  @Override public void onSuccess() {
    CommonTool.setSharePreference((Context) loginView, "isLogin", "true");
    UserInfo userInfo = new UserInfo();
    userInfo.setName("承运人");
    userInfo.setDriver("司机");
    userInfo.setLicensePlate("湘A65560");
    userInfo.setUserName("test");
    CommonTool.setSharePreference((Context) loginView, "name", userInfo.getName());
    CommonTool.setSharePreference((Context) loginView, "driver", userInfo.getDriver());
    CommonTool.setSharePreference((Context) loginView, "licensePlate", userInfo.getLicensePlate());
    if (loginView != null) {
      loginView.navigateToHome();
    }
  }

  public void initUsername() {
    String isCheck = CommonTool.getSharePreference((Context) loginView, "rememberUser");
    if (StringTool.isNotNull(isCheck) && Boolean.valueOf(isCheck)) {
      String userName = CommonTool.getSharePreference((Context) loginView, "userName");
      loginView.setRememberCheck(true);
      loginView.fillRememberUserName(userName);
    } else {
      loginView.setRememberCheck(false);
    }
  }
}
