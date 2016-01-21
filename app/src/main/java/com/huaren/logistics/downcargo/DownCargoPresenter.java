package com.huaren.logistics.downcargo;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;
import android.widget.Toast;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.bean.CustomerInfo;
import com.huaren.logistics.dao.CustomerInfoDao;
import com.huaren.logistics.entity.Customer;
import com.huaren.logistics.entity.Goods;
import com.huaren.logistics.util.CommonTool;
import com.huaren.logistics.util.UiTool;
import com.huaren.logistics.util.http.BaseHandler;
import com.huaren.logistics.util.webservice.WebServiceConnect;
import com.huaren.logistics.util.webservice.WebServiceHandler;
import com.huaren.logistics.util.webservice.WebServiceParam;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class DownCargoPresenter {

  private static final int REFRESH = 1;

  private IDownCargoView downCargoView;

  private List<Customer> customerList;

  private DownCargoAdapter adapter;
  /**
   * 当前页数
   */
  int currPage = 1;
  /**
   * 总页数
   */
  int totalPage = 2;
  /**
   * 每页显示多少条
   */
  String length = "8";

  public WebServiceHandler handler;
  protected WebServiceConnect webServiceConnect = new WebServiceConnect();

  public DownCargoPresenter(final IDownCargoView downCargoView) {
    this.downCargoView = downCargoView;

    handler = new WebServiceHandler((Context) downCargoView) {
      @Override public void handleFirst() {

      }

      @Override public void handleMsg(int returnCode, String detail) {
        switch (returnCode) {
          case 1:
            UiTool.showToast((Context) downCargoView, "调用成功！");
            addCustomerInfo(detail);
            break;
        }
      }
    };
  }

  private void addCustomerInfo(String detail) {
    parseXml(detail);
    insertCustomer();
  }

  private void insertCustomer() {
    List<Customer> customerList = new ArrayList<>();
    Customer customer = new Customer();
    customer.setName("客户名称1");
    customer.setAddress("地址名称1");
    customer.setCode("code1");

    List<Goods> goodsList = new ArrayList<>();
    Goods goods = new Goods();
    goods.setName("货物1");
    goods.setId("001");
    goods.setBarcode("bar001");
    goodsList.add(goods);

    goods = new Goods();
    goods.setName("货物2");
    goods.setId("002");
    goods.setBarcode("bar002");
    goodsList.add(goods);

    goods = new Goods();
    goods.setName("货物3");
    goods.setId("003");
    goods.setBarcode("bar003");
    goodsList.add(goods);
    customer.setUnloadedGoodsList(goodsList);
    customerList.add(customer);

    customer = new Customer();
    customer.setName("客户名称2");
    customer.setAddress("地址名称2");
    customer.setCode("code2");

    goodsList = new ArrayList<>();
    goods = new Goods();
    goods.setName("货物11");
    goods.setId("0011");
    goods.setBarcode("bar0011");
    goodsList.add(goods);

    goods = new Goods();
    goods.setName("货物21");
    goods.setId("0021");
    goods.setBarcode("bar0021");
    goodsList.add(goods);

    goods = new Goods();
    goods.setName("货物31");
    goods.setId("0031");
    goods.setBarcode("bar0031");
    goodsList.add(goods);
    customer.setUnloadedGoodsList(goodsList);
    customerList.add(customer);

    int count = 0;
    CustomerInfoDao customerInfoDao =
        LogisticsApplication.getInstance().getDaoSession().getCustomerInfoDao();
    for (int i = 0; i < customerList.size(); i++) {
      Customer customer1 = customerList.get(i);
      List<CustomerInfo> customerInfoList = customerInfoDao.queryBuilder()
          .where(CustomerInfoDao.Properties.Code.eq(customer1.getCode())).list();
      if(customerInfoList == null) {
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setCode(customer1.getCode());
        customerInfo.setName(customer1.getName());
        customerInfo.setAddress(customer1.getAddress());
        customerInfoDao.insert(customerInfo);
      }
    }

    List<CustomerInfo> list = customerInfoDao.loadAll();
    System.out.println(list);
    String time = CommonTool.parseCurrDateToString("yyyy-MM-dd HH:mm:ss");
    String info = "更新了" + count + "条记录";
    downCargoView.showUpdateView(time, info);
  }

  private void parseXml(String detail) {
    AssetManager am = downCargoView.getAssetManager();
    try {
      InputStream is = am.open("test.xml");
      XmlPullParser parser = Xml.newPullParser();
      parser.setInput(is, "utf-8");
      //  产生第一个事件
      int eventType = parser.getEventType();
      //  当文档结束事件时退出循环
      Customer customer = null;
      while (eventType != XmlPullParser.END_DOCUMENT) {
        switch (eventType) {
          // 开始文档
          case XmlPullParser.START_DOCUMENT:
            // new 集合，方便于添加元素
            customerList = new ArrayList<>();
            break;
          // 开始标记
          case XmlPullParser.START_TAG:
            // 获得当前节点名（标记名）
            String name = parser.getName();
            if ("CooperateID".equals(name)) {
              customer = new Customer();
              // 获得当前节点名的第一个属性的值
              customer.setCode(parser.nextText());
            }
            if (customer != null) {
              if ("CooperateName".equals(name)) {
                // 获取当前节点名的文本节点的值
                customer.setName(parser.nextText());
              }
            }
            break;
          // 结束标记
          case XmlPullParser.END_TAG:
            // 当结束标记为 person时
            if ("Data".equals(parser.getName())) {
              customerList.add(customer);
              // 清空。方便于加载第二个
              customer = null;
            }
            break;
        }
        // 获得解析器中的下一个事件
        eventType = parser.next();
      }
      System.out.println(customerList);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (XmlPullParserException e) {
      e.printStackTrace();
    }
  }

  public void initList(boolean refresh) {
    if (CommonTool.isWifiConnected((Context) downCargoView)) {
      if (customerList != null && !customerList.isEmpty()) {
        if (refresh) {
          if (downCargoView.isFooterShown() && currPage < totalPage) {
            currPage++;
            adapter.addInfo(customerList);
          }
          adapter.notifyDataSetChanged();
          mHandler.sendEmptyMessageDelayed(REFRESH, 20);
        } else {
          adapter = new DownCargoAdapter((Context) downCargoView, customerList);
          downCargoView.initPullToRefreshListView(adapter);
        }
      } else {
        adapter = new DownCargoAdapter((Context) downCargoView, new ArrayList<Customer>());
        adapter.notifyDataSetChanged();
        mHandler.sendEmptyMessageDelayed(REFRESH, 20);
      }

      if (!refresh) {// 第一次加载
        //if (result.getPageInfo() != null) {
        //  int totalSize = Integer.parseInt(result.getPageInfo().getTotalSize());
        //  int lenth = Integer.parseInt(result.getPageInfo().getLength());
        //  totalPage = totalSize % lenth == 0 ? totalSize / lenth : totalSize / lenth + 1;
        //}
      }
    } else {
      downCargoView.hideUpdateView();
    }
  }

  public void downloadData() {
    Map params = new HashMap();
    params.put("S_PDTG_EMPLOPCODE", "admin");
    params.put("date", "2010");
    String method = "Getdingdan";
    String action = "http://tempuri.org/Getdingdan";
    WebServiceParam webServiceParam =
        new WebServiceParam((Context) downCargoView, params, method, action, handler, 1);
    webServiceConnect.addNet(webServiceParam);
  }

  Handler mHandler = new Handler() {
    public void handleMessage(Message msg) {
      try {
        switch (msg.what) {
          case 1:
            downCargoView.onRefreshComplete();
            break;
          case 2:
            Toast.makeText((Context) downCargoView, "获取订单信息成功", Toast.LENGTH_SHORT).show();
            break;
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  };
}
