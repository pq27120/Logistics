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
import com.huaren.logistics.util.UiTool;
import com.huaren.logistics.util.webservice.WebServiceConnect;
import com.huaren.logistics.util.webservice.WebServiceHandler;
import com.huaren.logistics.util.webservice.WebServiceParam;
import java.util.HashMap;
import java.util.Map;

public class LoginPresenter{

  private LoginView loginView;
  public WebServiceHandler handler;
  protected WebServiceConnect webServiceConnect = new WebServiceConnect();

  public LoginPresenter(final LoginView loginView) {
    this.loginView = loginView;

    handler = new WebServiceHandler((Context) loginView) {
      @Override public void handleFirst() {

      }

      @Override public void handleMsg(int returnCode, String detail) {
        switch (returnCode) {
          case 1:
            UiTool.showToast((Context) loginView, "调用成功！");
            CommonTool.setSharePreference((Context) loginView, "isLogin", "true");
            UserInfo userInfo = new UserInfo();
            userInfo.setUserName("test");
            if (loginView != null) {
              loginView.navigateToHome();
            }
            break;
        }
      }
    };
  }

  public void validateCredentials(String username, String password) {
    if (loginView != null) {
      loginView.showProgress();
    }
    Map params = new HashMap();
    params.put("S_PDTG_EMPLOPCODE", "admin");
    params.put("date", "2010");
    String method = "Getdingdan";
    String action = "http://tempuri.org/Getdingdan";
    WebServiceParam webServiceParam =
        new WebServiceParam((Context) loginView, params, method, action, handler, 1);
    webServiceConnect.addNet(webServiceParam);
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
