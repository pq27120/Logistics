package com.huaren.logistics.login;

import android.os.Handler;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.huaren.logistics.util.http.BaseHandler;
import com.huaren.logistics.util.http.NetConnect;
import com.huaren.logistics.util.http.NetParam;
import java.util.HashMap;
import java.util.Map;

public class LoginInteractorImpl implements LoginInteractor {

    public BaseHandler handler;
    protected NetConnect netConnect = new NetConnect();

    @Override
    public void login(final String username, final String password, final OnLoginFinishedListener listener) {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                boolean error = false;
                Map params = new HashMap();
                //params.put("token", GlobalParams.getUserPassword());
                //params.put("idList", JSON.toJSON(memList));
                //String str = JSON.toJSON(params).toString();
                //StringBuilder builder = new StringBuilder();
                //builder.append("/Dating/queryUsersTeamMembers");
                //builder.append("?params=" + str);
                //NetParam netParam = new NetParam(this, builder.toString(), handler, 1);
                //netConnect.addNet(netParam);
                if (TextUtils.isEmpty(username)){
                    listener.onUsernameError();
                    error = true;
                }
                if (TextUtils.isEmpty(password)){
                    listener.onPasswordError();
                    error = true;
                }
                if (!error){
                    listener.onSuccess();
                }
            }
        }, 2000);
    }
}
