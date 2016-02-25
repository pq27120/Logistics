package com.huaren.logistics.splash;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.bean.LogisticsUser;
import com.huaren.logistics.common.Constant;
import com.huaren.logistics.entity.UpdateInfo;
import com.huaren.logistics.util.CommonTool;
import com.huaren.logistics.util.StreamTools;
import com.huaren.logistics.util.StringTool;
import com.huaren.logistics.util.UiTool;
import com.huaren.logistics.util.webservice.WebServiceConnect;
import com.huaren.logistics.util.webservice.WebServiceHandler;
import com.huaren.logistics.util.webservice.WebServiceParam;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class SplashPresenter {

  private ISplashView iSplashView;

  protected static final int SHOW_UPDATE_DIALOG = 1000;
  protected static final int ENTER_HOME = 1001;
  protected static final int URL_ERROR = 1002;
  protected static final int NETWORK_ERROR = 1003;
  protected static final int JSON_ERROR = 1004;

  private UpdateInfo updateInfo;

  public WebServiceHandler webServiceHandler;
  protected WebServiceConnect webServiceConnect = new WebServiceConnect();

  public SplashPresenter(final ISplashView iSplashView) {
    this.iSplashView = iSplashView;

    webServiceHandler = new WebServiceHandler((Context) iSplashView) {
      @Override public void handleFirst() {
      }

      @Override public void handleMsg(int returnCode, Object detail) {
        switch (returnCode) {
          case 1:
            UiTool.showToast((Context) iSplashView, "调用成功！");
            initUserInfo(detail);
            break;
        }
      }
    };
  }

  private void initUserInfo(Object detail) {
    ArrayList<LogisticsUser> logisticsUserList = parserUserInfoXml(detail);
    cleanLogisticsUserTable();
    initLogisticsUserTable(logisticsUserList);
  }

  private void initLogisticsUserTable(ArrayList<LogisticsUser> logisticsUserList) {
    LogisticsApplication.getInstance().getLogisticsUserDao().insertInTx(logisticsUserList);
  }

  private void cleanLogisticsUserTable() {
    LogisticsApplication.getInstance().getLogisticsUserDao().deleteAll();
  }

  private ArrayList<LogisticsUser> parserUserInfoXml(Object detail) {
    ArrayList<LogisticsUser> logisticsUserList = new ArrayList<>();;
    SoapObject soapObject = (SoapObject) detail;
    String xml = soapObject.getPropertyAsString("GetUsersResult");
    try {
      XmlPullParser parser = Xml.newPullParser();
      parser.setInput(new StringReader(xml));
      //  产生第一个事件
      int eventType = parser.getEventType();
      //  当文档结束事件时退出循环
      LogisticsUser logisticsUser = null;
      while (eventType != XmlPullParser.END_DOCUMENT) {
        switch (eventType) {
          // 开始文档
          case XmlPullParser.START_DOCUMENT:
            break;
          // 开始标记
          case XmlPullParser.START_TAG:
            // 获得当前节点名（标记名）
            String name = parser.getName();
            if ("Username".equals(name)) {
              logisticsUser = new LogisticsUser();
              // 获得当前节点名的第一个属性的值
              logisticsUser.setUserName(parser.nextText());
            }
            if (logisticsUser != null) {
              if ("Pwd".equals(name)) {
                // 获取当前节点名的文本节点的值
                logisticsUser.setPwd(parser.nextText());
              }
            }
            break;
          // 结束标记
          case XmlPullParser.END_TAG:
            // 当结束标记为 person时
            if ("Data".equals(parser.getName())) {
              logisticsUserList.add(logisticsUser);
              // 清空。方便于加载第二个
              logisticsUser = null;
            }
            break;
        }
        // 获得解析器中的下一个事件
        eventType = parser.next();
      }
      CommonTool.showLog(logisticsUserList.toString());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (XmlPullParserException e) {
      e.printStackTrace();
    }
    return logisticsUserList;
  }

  public void fetchUserData() {
    Map params = new HashMap();
    params.put("S_PDTG_EMPLOPCODE", "admin");
    params.put("date", "2010");
    String method = "GetUsers";
    String action = "http://tempuri.org/GetUsers";
    WebServiceParam webServiceParam =
        new WebServiceParam((Context) iSplashView, params, method, action, webServiceHandler, 1);
    webServiceConnect.addNet(webServiceParam);
  }

  public void onDestroy() {
    iSplashView = null;
  }

  void checkUpdate() {
    new Thread() {
      public void run() {
        Message mes = Message.obtain();
        long startTime = System.currentTimeMillis();
        try {
          // 获取一个消息对象
          URL url = new URL(Constant.UPDATE_URL);
          // 联网
          HttpURLConnection conn = (HttpURLConnection) url.openConnection();
          conn.setRequestMethod("GET");
          conn.setConnectTimeout(3000);
          conn.setReadTimeout(30000);
          int code = conn.getResponseCode();
          if (code == 200) {
            // 联网成功
            InputStream is = conn.getInputStream();
            // 把流转成string
            String result = StreamTools.readFromStream(is);
            CommonTool.showLog("联网成功了" + result);
            // json解析
            updateInfo = JSON.parseObject(result, UpdateInfo.class);
            int version = CommonTool.getVersion((Context) iSplashView);
            CommonTool.showLog(
                result + "==" + Integer.parseInt(updateInfo.getCode()) + "==" + version);
            // 检验是否有新版本
            if (version >= Integer.parseInt(updateInfo.getCode())) {
              // 版本一致,无需更新，进入主页
              mes.what = ENTER_HOME;
            } else {
              // 有新版本，弹出更新提示
              mes.what = SHOW_UPDATE_DIALOG;
            }
          } else {
            mes.what = NETWORK_ERROR;
          }
        } catch (MalformedURLException e) {
          // TODO Auto-generated catch block
          mes.what = URL_ERROR;
          e.printStackTrace();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          mes.what = NETWORK_ERROR;
          e.printStackTrace();
        } catch (com.alibaba.fastjson.JSONException e) {
          // TODO Auto-generated catch block
          mes.what = JSON_ERROR;
          e.printStackTrace();
        } finally {
          long endTime = System.currentTimeMillis();
          long dTime = endTime - startTime;
          if (dTime < 2000) {
            try {
              Thread.sleep(2000 - dTime);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
          handler.sendMessage(mes);
        }
      }
    }.start();
  }

  private Handler handler = new Handler() {
    @Override public void handleMessage(Message msg) {
      super.handleMessage(msg);
      String isLogin = CommonTool.getSharePreference((Context) iSplashView, "isLogin");
      switch (msg.what) {
        case SHOW_UPDATE_DIALOG:
          CommonTool.showLog("显示升级的对话框");
          iSplashView.showUpdateDialog(updateInfo);
          break;
        case ENTER_HOME:
          fetchUserData();
          if (StringTool.isNotNull(isLogin) && Boolean.valueOf(isLogin)) {
            iSplashView.enterMain();
          } else {
            iSplashView.enterLogin();
          }
          break;
        case URL_ERROR:
          if (StringTool.isNotNull(isLogin) && Boolean.valueOf(isLogin)) {
            iSplashView.enterMain();
          } else {
            iSplashView.enterLogin();
          }
          // Toast.makeText(getApplicationContext(), "URL错误", 0).show();
          break;
        case NETWORK_ERROR:
          if (StringTool.isNotNull(isLogin) && Boolean.valueOf(isLogin)) {
            iSplashView.enterMain();
          } else {
            iSplashView.enterLogin();
          }
          // Toast.makeText(SplashActivity.this, "网络异常", 0).show();
          break;
        case JSON_ERROR:
          if (StringTool.isNotNull(isLogin) && Boolean.valueOf(isLogin)) {
            iSplashView.enterMain();
          } else {
            iSplashView.enterLogin();
          }
          // Toast.makeText(SplashActivity.this, "JSON解析出错", 0).show();
          break;
        default:
          break;
      }
    }
  };

  public void updateApk() {
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      String savePath = removeFile();
      downloadApk(savePath);
    } else {
      Toast.makeText((Context) iSplashView, "没有sdcard，请安装上在试", Toast.LENGTH_SHORT).show();
    }
  }

  private void downloadApk(String savePath) {
    FinalHttp finalhttp = new FinalHttp();
    finalhttp.download(updateInfo.getApkurl(), savePath, new AjaxCallBack<File>() {
      @Override public void onFailure(Throwable t, int errorNo, String strMsg) {
        t.printStackTrace();
        Toast.makeText((Context) iSplashView, "下载失败", Toast.LENGTH_SHORT).show();
        super.onFailure(t, errorNo, strMsg);
        iSplashView.finishActivity();
      }

      @Override public void onLoading(long count, long current) {
        super.onLoading(count, current);
        iSplashView.showUpdateProgress();
        // 当前下载百分比
        int progress = (int) (current * 100 / count);
        iSplashView.changeProgress(progress);
      }

      @Override public void onSuccess(File t) {
        super.onSuccess(t);
        iSplashView.hideProgress();
        installAPK(t);
      }

      /**
       * 安装APK
       *
       * @param t
       */
      private void installAPK(File t) {
        //创建URI
        Uri uri = Uri.fromFile(t);
        //创建Intent意图
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//启动新的activity
        //设置Uri和类型
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        //执行安装
        ((Context) iSplashView).startActivity(intent);
        iSplashView.finishActivity();
      }
    });
  }

  private String removeFile() {
    final String savePath =
        Environment.getExternalStorageDirectory().getAbsolutePath() + "/logistics.apk";
    File f = new File(savePath);
    if (f.exists()) {
      f.delete();
    }
    return savePath;
  }
}
