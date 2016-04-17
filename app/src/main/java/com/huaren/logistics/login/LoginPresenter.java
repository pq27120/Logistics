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
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.bean.LogisticsUser;
import com.huaren.logistics.dao.LogisticsUserDao;
import com.huaren.logistics.util.CommonTool;
import com.huaren.logistics.util.MD5Util;
import com.huaren.logistics.util.StringTool;
import com.huaren.logistics.util.UiTool;
import java.util.List;

public class LoginPresenter {

  private LoginView loginView;

  public LoginPresenter(final LoginView loginView) {
    this.loginView = loginView;
  }

  public void validateCredentials(String username, String password) {
    if (loginView != null) {
      loginView.showProgress();
    }
    LogisticsUserDao userDao = LogisticsApplication.getInstance().getLogisticsUserDao();
    String md5Pass = MD5Util.MD5(password);
    List<LogisticsUser> list = userDao.queryBuilder()
        .where(LogisticsUserDao.Properties.UserName.eq(username)
            ,LogisticsUserDao.Properties.Pwd.eq(md5Pass)
        )
        .list();
    if (list != null && !list.isEmpty()) {
      LogisticsUser logisticsUser = list.get(0);
      CommonTool.setSharePreference((Context) loginView, "curUserName", username);
      CommonTool.setSharePreference((Context) loginView, "driverId", logisticsUser.getDriverId());
      loginView.navigateToHome();
    }else{
      LogisticsApplication.getInstance().getSoundPoolUtil().playWrong();
      UiTool.showToast((Context)loginView, "用户名或密码错误，请重新输入！");
      loginView.clearPasswordEt();
    }
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