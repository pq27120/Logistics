package com.huaren.logistics.util.webservice;

import android.os.Handler;
import android.os.Message;
import com.huaren.logistics.common.Constant;
import com.huaren.logistics.util.CommonTool;
import com.huaren.logistics.util.http.NetConnect;
import java.util.Map;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WebServiceNet extends Thread {
  private SoapObject content;
  private WebServiceParam webServiceParam;
  private Handler parentHandler;
  private SoapSerializationEnvelope envelope;
  private int timeOut = 15000;//

  private boolean isCancel = false; // 是否取消连接

  public WebServiceNet(WebServiceParam webServiceParam) {
    this.webServiceParam = webServiceParam;
  }

  // 线程执行
  public void run() {
    try {
      getNetData();
    } catch (Exception e) {
      CommonTool.showLog(e.getMessage());
      e.printStackTrace();
      notifyUI(NetConnect.NET_ERROR, null);
    }
  }

  /** 连接网络获取数据 */
  public void getNetData() {
    boolean connectStatue = call();
    if (connectStatue == true) {
      if (getNetContent()) {
        if (isCancel) return;
        // 取得数据
        notifyUI(webServiceParam.getResult(), content);
        return;
      }
    }
    notifyUI(NetConnect.NET_ERROR, null);
  }

  private boolean call() {
    boolean getFlag = false;
    SoapObject soapObject = new SoapObject(Constant.ADDRESS_NAMESPACE, webServiceParam.method);
    soapObject.addProperty("username", Constant.USER_NAME);
    soapObject.addProperty("pwd", Constant.PASSWORD);
    Map<String, String> map = webServiceParam.getInParamMap();
    for (String s : map.keySet()) {
      soapObject.addProperty(s, map.get(s));
    }
    envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    envelope.bodyOut = soapObject;
    envelope.dotNet = true;
    envelope.setOutputSoapObject(soapObject);
    HttpTransportSE httpTransportSE = new HttpTransportSE(Constant.WEBSERVICE_URL, timeOut);
    try {
      httpTransportSE.call(webServiceParam.action, envelope);
      getFlag = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return getFlag;
  }

  /** 连接后获取内容 */
  private boolean getNetContent() {
    boolean getNetDataFlag = false;
    try {
      if (envelope.bodyIn instanceof SoapFault) {
        CommonTool.showLog(envelope.bodyIn.toString());
      } else {
        SoapObject soapObject = (SoapObject) envelope.bodyIn;
        content = soapObject;
      }
      getNetDataFlag = true;
    } catch (Exception e) {
      CommonTool.showLog(e.getMessage());
      e.printStackTrace();
    }
    return getNetDataFlag;
  }

  /** 通知 响应界面UI与连接集合 */
  public void notifyUI(int resultState, SoapObject dataMsg) {
    CommonTool.showLog("dataMsg=" + dataMsg);
    if (isCancel) return;
    Message msg = new Message();
    msg.what = resultState;
    if (dataMsg != null) {
      msg.obj = dataMsg;
    }
    // 通知界面响应请求
    webServiceParam.getNetHandler().sendMessage(msg);
    // 通知缓存清除该请求
    if (parentHandler != null) {
      msg = new Message();
      msg.what = 1;
      msg.obj = webServiceParam;
      parentHandler.sendMessage(msg);
    }
  }

  public Handler getParentHandler() {
    return parentHandler;
  }

  public void setParentHandler(Handler parentHandler) {
    this.parentHandler = parentHandler;
  }
}
