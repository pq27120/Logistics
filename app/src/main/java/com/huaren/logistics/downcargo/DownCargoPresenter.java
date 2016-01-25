package com.huaren.logistics.downcargo;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;
import android.widget.Toast;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.bean.Customer;
import com.huaren.logistics.bean.Goods;
import com.huaren.logistics.dao.CustomerDao;
import com.huaren.logistics.dao.GoodsDao;
import com.huaren.logistics.util.CommonTool;
import com.huaren.logistics.util.UiTool;
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

  private IDownCargoView downCargoView;

  private List<Customer> customerList;

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
    goods.setGoodsName("货物1");
    goods.setBarCode("001");
    goods.setIsLoad(false);
    goods.setIsRemove(false);
    goodsList.add(goods);

    goods = new Goods();
    goods.setGoodsName("货物2");
    goods.setBarCode("002");
    goods.setIsLoad(false);
    goods.setIsRemove(false);
    goodsList.add(goods);

    goods = new Goods();
    goods.setGoodsName("货物3");
    goods.setBarCode("003");
    goods.setIsLoad(false);
    goods.setIsRemove(false);
    goodsList.add(goods);
    customer.setGoods(goodsList);
    customerList.add(customer);

    customer = new Customer();
    customer.setName("客户名称2");
    customer.setAddress("地址名称2");
    customer.setCode("code2");

    goodsList = new ArrayList<>();
    goods = new Goods();
    goods.setGoodsName("货物11");
    goods.setBarCode("0011");
    goods.setIsLoad(false);
    goods.setIsRemove(false);
    goodsList.add(goods);

    goods = new Goods();
    goods.setGoodsName("货物21");
    goods.setBarCode("0021");
    goods.setIsLoad(false);
    goods.setIsRemove(false);
    goodsList.add(goods);

    goods = new Goods();
    goods.setGoodsName("货物31");
    goods.setBarCode("0031");
    goods.setIsLoad(false);
    goods.setIsRemove(false);
    goodsList.add(goods);
    customer.setGoods(goodsList);
    customerList.add(customer);

    int customerCount = 0;
    int goodsCount = 0;
    CustomerDao customerDao = LogisticsApplication.getInstance().getDaoSession().getCustomerDao();
    GoodsDao goodsDao = LogisticsApplication.getInstance().getDaoSession().getGoodsDao();
    if (customerList != null && !customerList.isEmpty()) {
      for (int i = 0; i < customerList.size(); i++) {
        Customer customer1 = customerList.get(i);
        long customerQueryCount = customerDao.queryBuilder()
            .where(CustomerDao.Properties.Code.eq(customer1.getCode()))
            .buildCount().count();
        if (customerQueryCount <= 0) {
          customerDao.insert(customer1);
          customerCount++;
        }
        List<Goods> goodsList1 = customer1.getGoods();
        if (goodsList1 != null && !goodsList1.isEmpty()) {
          for (int j = 0; j < goodsList1.size(); j++) {
            Goods goods1 = goodsList1.get(j);
            long goodsQueryCount = goodsDao.queryBuilder()
                .where(GoodsDao.Properties.BarCode.eq(goods1.getBarCode()))
                .buildCount().count();
            if (goodsQueryCount == 0) {
              goods1.setCustomerId(customer1.getId());
              goodsDao.insert(goods1);
              goodsCount++;
            }
          }
        }
      }
    }

    List<Customer> list = customerDao.loadAll();
    System.out.println(list);
    String time = CommonTool.parseCurrDateToString("yyyy-MM-dd HH:mm:ss");
    String info = "更新了" + customerCount + "条客户记录，" + goodsCount + " 条货物记录";
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
