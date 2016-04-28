package com.huaren.logistics.util.webservice;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.huaren.logistics.util.CommonTool;
import com.huaren.logistics.util.StringTool;
import com.huaren.logistics.util.UiTool;
import com.huaren.logistics.util.http.NetConnect;
import org.json.JSONObject;

public abstract class WebServiceHandler extends Handler {
  public Context context;

  public WebServiceHandler(Context context) {
    this.context = context;
  }

  public void handleMessage(Message msg) {
    try {
      handleFirst();
      switch (msg.what) {
        case NetConnect.NET_ERROR: // 网络失败
          UiTool.showToast(context, "网络连接错误！");
          handleMsg(msg.what, "");
          break;
        default: // 有返回
          //JSONObject jsonObject = new JSONObject(msg.obj.toString());
          //if (msg.obj.toString().contains("results")) {
          //  String detail = jsonObject.getString("results");
          //  if (StringTool.isNotNull(detail)) {
          //    if (msg.obj.toString().contains("result")) {
          //      String other = jsonObject.getString("result");
          //      if (StringTool.isNotNull(detail)) {
          //        handOthers(other);
          //      }
          //    }
          //    if ("[]".equals(detail)) {
          //      handNullMsg(msg.what);
          //      UiTool.showToast(context, "暂无数据");
          //    } else {
          handleMsg(msg.what, msg.obj);
          //    }
          //    return;
          //  }
          //}
          //if (msg.obj.toString().contains("result")) {
          //  String detail = jsonObject.getString("result");
          //  if (StringTool.isNotNull(detail)) {
          //    handleMsg(msg.what, detail);
          //    return;
          //  }
          //}
          //String error = jsonObject.getString("error");
          //if (StringTool.isNotNull(error)) {
          //  JSONObject errObject = new JSONObject(error);
          //  String errorMessage = errObject.getString("errorMessage");
          //  if (StringTool.isNotNull(errorMessage)) {
          //    UiTool.showToast(context, errorMessage);
          //  }
          //} else {
          //  handNullMsg(msg.what);
          //  UiTool.showToast(context, "暂无数据");
          //}
          break;
      }
    } catch (Exception e) {
      CommonTool.showLog(e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * 有消息返回时,立即运行的操作<br/>
   * 例如:进度条消失等
   */
  public abstract void handleFirst();

  /**
   * 成功返回 自定义的操作<br/>
   * 例如: 绑定显示数据对象
   *
   * @param returnCode 成功返回的returnCode
   * @param detail 返回的解析后的对象
   */
  public abstract void handleMsg(int returnCode, Object detail);

  public void handNullMsg(int returnCode) {
  }

  public void handOthers(Object object) {
  }
}
