package com.huaren.logistics.downcargo;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;
import android.widget.Toast;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.bean.Customer;
import com.huaren.logistics.bean.LogisticsOrder;
import com.huaren.logistics.bean.OrderDetail;
import com.huaren.logistics.dao.CustomerDao;
import com.huaren.logistics.dao.LogisticsOrderDao;
import com.huaren.logistics.dao.OrderDetailDao;
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
    for (int custIndex = 1; custIndex < 10; custIndex++) {
      Customer customer = new Customer();
      customer.setName("客户名称" + custIndex);
      customer.setAddress("地址名称" + custIndex);
      customer.setCustomerId("code" + custIndex);
      customer.setPassword("123456");
      List<LogisticsOrder> orderList = new ArrayList<>();
      for (int orderIndex = 1; orderIndex < 11; orderIndex++) {
        LogisticsOrder order = new LogisticsOrder();
        order.setCustomerId(customer.getCustomerId());
        order.setOrderName("客户" + custIndex + "订单" + orderIndex);
        order.setOrderId("code" + custIndex + "00" + orderIndex);
        order.setOrderStatus("1");

        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (int detailIndex = 1; detailIndex < 11; detailIndex++) {
          OrderDetail orderDetail = new OrderDetail();
          orderDetail.setOrderId(order.getOrderId());
          orderDetail.setDetailId("code" + custIndex + "00" + orderIndex + "00" + detailIndex);
          orderDetail.setDetailName("客户" + custIndex + "订单" + orderIndex + "货物" + detailIndex);
          orderDetail.setDetailStatus("1");
          orderDetailList.add(orderDetail);
        }
        order.setOrderDetails(orderDetailList);
        orderList.add(order);
      }
      customer.setOrders(orderList);
      customerList.add(customer);
    }

    CommonTool.showLog(customerList.toString());

    int customerCount = 0;
    int orderCount = 0;
    int orderDetailCount = 0;
    CustomerDao customerDao = LogisticsApplication.getInstance().getDaoSession().getCustomerDao();
    LogisticsOrderDao logisticsOrderDao = LogisticsApplication.getInstance().getDaoSession().getLogisticsOrderDao();
    OrderDetailDao orderDetailDao = LogisticsApplication.getInstance().getDaoSession().getOrderDetailDao();
    if (customerList != null && !customerList.isEmpty()) {
      for (int i = 0; i < customerList.size(); i++) {
        Customer customer1 = customerList.get(i);
        long customerQueryCount = customerDao.queryBuilder()
            .where(CustomerDao.Properties.CustomerId.eq(customer1.getCustomerId()))
            .buildCount()
            .count();
        if (customerQueryCount <= 0) {
          customerDao.insert(customer1);
          customerCount++;
        }
        List<LogisticsOrder> orderList = customer1.getOrders();
        if (orderList != null && !orderList.isEmpty()) {
          for (int j = 0; j < orderList.size(); j++) {
            LogisticsOrder order1 = orderList.get(j);
            CommonTool.showLog(order1.toString());
            long orderQueryCount = logisticsOrderDao.queryBuilder()
                .where(LogisticsOrderDao.Properties.OrderId.eq(order1.getOrderId()))
                .buildCount()
                .count();
            if (orderQueryCount == 0) {
              logisticsOrderDao.insert(order1);
              orderCount++;
            }

            List<OrderDetail> orderDetailList1 = order1.getOrderDetails();
            for (int k = 0; k < orderDetailList1.size(); k++) {
              OrderDetail orderDetail = orderDetailList1.get(k);
              long orderDetailQueryCount = orderDetailDao.queryBuilder()
                  .where(OrderDetailDao.Properties.DetailId.eq(orderDetail.getDetailId()))
                  .buildCount()
                  .count();
              if (orderDetailQueryCount == 0) {
                orderDetailDao.insert(orderDetail);
                orderDetailCount++;
              }
            }
          }
        }
      }
    }

    List<Customer> list = customerDao.loadAll();
    System.out.println(list);
    String time = CommonTool.parseCurrDateToString("yyyy-MM-dd HH:mm:ss");
    String info = "更新了" + customerCount + "条客户记录，" + orderCount + " 条订单主单记录，" + orderDetailCount + "条订单详单记录";
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
              customer.setCustomerId(parser.nextText());
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
