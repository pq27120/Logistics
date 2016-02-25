package com.huaren.logistics.util.webservice;

import android.content.Context;
import android.os.Handler;
import java.util.Map;

public class WebServiceParam {
  private Context context;
  private Map<String,String> inParamMap;
  private Handler netHandler;
  private int result;
  private boolean cachesFlag = false;
  public Map params;
  public String method;
  public String action;

  public WebServiceParam(Context context, Map<String, String> inParamMap,String method,String action,
      Handler handler, int result) {
    this.context = context;
    this.inParamMap = inParamMap;
    this.netHandler = handler;
    this.result = result;
    this.method = method;
    this.action = action;
  }

  public Context getContext() {
    return context;
  }

  public void setContext(Context context) {
    this.context = context;
  }

  public Map<String, String> getInParamMap() {
    return inParamMap;
  }

  public void setInParamMap(Map<String, String> inParamMap) {
    this.inParamMap = inParamMap;
  }

  public Handler getNetHandler() {
    return netHandler;
  }

  public void setNetHandler(Handler netHandler) {
    this.netHandler = netHandler;
  }

  public int getResult() {
    return result;
  }

  public void setResult(int result) {
    this.result = result;
  }

  public boolean isCachesFlag() {
    return cachesFlag;
  }

  public void setCachesFlag(boolean cachesFlag) {
    this.cachesFlag = cachesFlag;
  }

  public Map getParams() {
    return params;
  }

  public void setParams(Map params) {
    this.params = params;
  }
}
