package com.huaren.logistics.util.webservice;

import android.os.Handler;
import android.os.Message;
import com.huaren.logistics.util.http.NetHttp;
import com.huaren.logistics.util.http.NetParam;
import java.util.Hashtable;
import java.util.Map;

public class WebServiceConnect {
  private Handler handler;
  private Map<String, WebServiceNet> netHttpMap;

  public WebServiceConnect() {
    netHttpMap = new Hashtable<String, WebServiceNet>();
    handler = new Handler() {
      public void handleMessage(Message msg) {
        try {
          WebServiceParam param = (WebServiceParam) msg.obj;
          switch (msg.what) {
            case 1:
              break;
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };
  }

  /** 新增网络连接 */
  public boolean addNet(WebServiceParam webServiceParam) {
    WebServiceNet webServiceNet = new WebServiceNet(webServiceParam);
    webServiceNet.setParentHandler(handler);
    netHttpMap.put(webServiceParam.action, webServiceNet); // 加入连接集合
    webServiceNet.start(); // 开始连接获取数据
    return true;
  }
}
