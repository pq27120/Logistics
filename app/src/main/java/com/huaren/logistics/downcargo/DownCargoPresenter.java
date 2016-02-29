package com.huaren.logistics.downcargo;

import android.content.Context;
import android.util.Xml;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.bean.Customer;
import com.huaren.logistics.bean.LogisticsOrder;
import com.huaren.logistics.bean.OrderDetail;
import com.huaren.logistics.bean.SysDic;
import com.huaren.logistics.bean.SysDicValue;
import com.huaren.logistics.dao.CustomerDao;
import com.huaren.logistics.dao.LogisticsOrderDao;
import com.huaren.logistics.dao.OrderDetailDao;
import com.huaren.logistics.dao.SysDicDao;
import com.huaren.logistics.dao.SysDicValueDao;
import com.huaren.logistics.util.CommonTool;
import com.huaren.logistics.util.UiTool;
import com.huaren.logistics.util.webservice.WebServiceConnect;
import com.huaren.logistics.util.webservice.WebServiceHandler;
import com.huaren.logistics.util.webservice.WebServiceParam;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class DownCargoPresenter {

  private IDownCargoView downCargoView;

  public WebServiceHandler handler;
  public WebServiceHandler dictHandler;
  protected WebServiceConnect webServiceConnect = new WebServiceConnect();
  private StringBuffer buffer = new StringBuffer("更新完成，更新了");

  public DownCargoPresenter(final IDownCargoView downCargoView) {
    this.downCargoView = downCargoView;

    handler = new WebServiceHandler((Context) downCargoView) {
      @Override public void handleFirst() {

      }

      @Override public void handleMsg(int returnCode, Object detail) {
        switch (returnCode) {
          case 1:
            UiTool.showToast((Context) downCargoView, "调用成功！");
            parseOrderInfo(detail);
            break;
        }
      }
    };

    dictHandler = new WebServiceHandler((Context) downCargoView) {
      @Override public void handleFirst() {

      }

      @Override public void handleMsg(int returnCode, Object detail) {
        switch (returnCode) {
          case 1:
            parseDictInfo(detail);
            break;
        }
      }
    };
  }

  private void parseOrderInfo(Object detail) {
    parseOrderXml(detail);
    String time = CommonTool.parseCurrDateToString("yyyy-MM-dd HH:mm:ss");
    downCargoView.showUpdateView(time, buffer.toString());
  }

  private void parseDictInfo(Object detail) {
    addDictionary();
  }

  private void addDictionary() {
    SysDic sysDic = new SysDic();
    sysDic.setId(1l);
    sysDic.setMyName("评价");

    List<SysDicValue> sysDicValueList = new ArrayList<>();
    SysDicValue sysDicValue = new SysDicValue();
    sysDicValue.setId(1l);
    sysDicValue.setDicId(1);
    sysDicValue.setMyName("1");
    sysDicValue.setMyDisplayValue("满意");
    sysDicValueList.add(sysDicValue);

    sysDicValue = new SysDicValue();
    sysDicValue.setId(2l);
    sysDicValue.setDicId(1);
    sysDicValue.setMyName("2");
    sysDicValue.setMyDisplayValue("一般");
    sysDicValueList.add(sysDicValue);

    sysDicValue = new SysDicValue();
    sysDicValue.setId(3l);
    sysDicValue.setDicId(1);
    sysDicValue.setMyName("3");
    sysDicValue.setMyDisplayValue("较差");
    sysDicValueList.add(sysDicValue);

    sysDic.setSysDicValueList(sysDicValueList);

    SysDicDao sysDicDao = LogisticsApplication.getInstance().getSysDicDao();
    sysDicDao.insertOrReplace(sysDic);

    SysDicValueDao sysDicValueDao = LogisticsApplication.getInstance().getSysDicValueDao();
    sysDicValueDao.insertOrReplaceInTx(sysDicValueList);
  }

  private void insertCustomer(Map<String, Customer> customerMap) {
    CustomerDao customerDao = LogisticsApplication.getInstance().getCustomerDao();
    for (Map.Entry<String, Customer> customerEntry : customerMap.entrySet()) {
      Customer customer = customerEntry.getValue();
      customer.setAddTime(new Date());
      customerDao.insertOrReplace(customer);
    }
    buffer.append(customerMap.entrySet().size()).append("条客户信息");
  }

  private void insertOrderDetail(List<OrderDetail> orderDetailList) {
    OrderDetailDao orderDetailDao = LogisticsApplication.getInstance().getOrderDetailDao();
    for (OrderDetail orderDetail : orderDetailList) {
      orderDetail.setAddTime(new Date());
      orderDetail.setDetailStatus("1");
      orderDetailDao.insertOrReplaceInTx(orderDetail);
    }
    buffer.append(",").append(orderDetailList.size()).append("条订单详情");
  }

  private void insertOrder(List<LogisticsOrder> logisticsOrderList) {
    LogisticsOrderDao logisticsOrderDao = LogisticsApplication.getInstance().getLogisticsOrderDao();
    for (LogisticsOrder logisticsOrder : logisticsOrderList) {
      logisticsOrder.setAddTime(new Date());
      logisticsOrder.setOrderStatus("1");
      logisticsOrderDao.insertOrReplaceInTx(logisticsOrder);
    }
    buffer.append(",").append(logisticsOrderList.size()).append("条订单");
  }

  private void parseOrderXml(Object detail) {
    Map<String, Customer> map = new HashMap<>();
    List<LogisticsOrder> logisticsOrderList = null;
    List<OrderDetail> orderDetailList = null;
    Customer customer = null;
    LogisticsOrder logisticsOrder = null;
    OrderDetail orderDetail = null;
    SoapObject soapObject = (SoapObject) detail;
    String xml = soapObject.getPropertyAsString("GetdingdanResult");
    try {
      XmlPullParser parser = Xml.newPullParser();
      parser.setInput(new StringReader(xml));
      //  产生第一个事件
      int eventType = parser.getEventType();
      //  当文档结束事件时退出循环
      while (eventType != XmlPullParser.END_DOCUMENT) {
        switch (eventType) {
          // 开始文档
          case XmlPullParser.START_DOCUMENT:
            // new 集合，方便于添加元素
            break;
          // 开始标记
          case XmlPullParser.START_TAG:
            // 获得当前节点名（标记名）
            String name = parser.getName();
            if ("Data".equals(name)) {
              logisticsOrderList = new ArrayList<>();
              orderDetailList = new ArrayList<>();
            }
            if ("ItemHeader".equals(name)) {
              logisticsOrder = new LogisticsOrder();
            }
            if ("CooperateID".equals(name)) {
              String cooperateId = parser.nextText();
              logisticsOrder.setCooperateID(cooperateId);
              if (map.containsKey(cooperateId)) {
                customer = map.get(cooperateId);
              } else {
                customer = new Customer();
                // 获得当前节点名的第一个属性的值
                customer.setCooperateId(cooperateId);
                map.put(cooperateId, customer);
              }
            }
            if ("CooperateName".equals(name)) {
              // 获取当前节点名的文本节点的值
              customer.setCooperateName(parser.nextText());
            }
            if ("DispatchNumber".equals(name)) {
              logisticsOrder.setDispatchNumber(parser.nextText());
            }
            if ("D_PDTG_DATE".equals(name)) {
              logisticsOrder.setDPdtgDate(parser.nextText());
            }
            if ("L_PDTG_BATCH".equals(name)) {
              logisticsOrder.setLPdtgBatch(parser.nextText());
            }
            if ("L_PDTG_MERDCATEG_OldKey".equals(name)) {
              logisticsOrder.setLPdtgMerdcategOldkey(parser.nextText());
            }
            if ("ORDERED".equals(name)) {
              logisticsOrder.setOrdered(parser.nextText());
            }
            if ("BOXNUMBER".equals(name)) {
              logisticsOrder.setBoxNumber(Integer.valueOf(parser.nextText()));
            }
            if ("PathName".equals(name)) {
              logisticsOrder.setPathName(parser.nextText());
            }
            if ("Item".equals(name)) {
              orderDetail = new OrderDetail();
              orderDetail.setOrdered(logisticsOrder.getOrdered());
            }
            if ("GoodsID".equals(name)) {
              orderDetail.setGoodsId(parser.nextText());
            }
            if ("GoodsName".equals(name)) {
              orderDetail.setGoodsName(parser.nextText());
            }
            if ("DispatchType".equals(name)) {
              orderDetail.setDispatchType(parser.nextText());
            }
            if ("I_groi_valunum".equals(name)) {
              orderDetail.setIGroiValunum(parser.nextText());
            }
            if ("LPN".equals(name)) {
              orderDetail.setLpn(parser.nextText());
            }
            break;
          // 结束标记
          case XmlPullParser.END_TAG:
            String endName = parser.getName();
            if ("Item".equals(endName)) {
              orderDetailList.add(orderDetail);
              orderDetail = null;
            }
            if ("ItemHeader".equals(endName)) {
              logisticsOrderList.add(logisticsOrder);
              logisticsOrder = null;
            }
            break;
        }
        // 获得解析器中的下一个事件
        eventType = parser.next();
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (XmlPullParserException e) {
      e.printStackTrace();
    }
    insertCustomer(map);
    insertOrder(logisticsOrderList);
    insertOrderDetail(orderDetailList);
  }

  public void downloadOrderData() {
    Map params = new HashMap();
    params.put("S_PDTG_EMPLOPCODE", "admin");
    params.put("date", "2010");
    String method = "Getdingdan";
    String action = "http://tempuri.org/Getdingdan";
    WebServiceParam webServiceParam =
        new WebServiceParam((Context) downCargoView, params, method, action, handler, 1);
    webServiceConnect.addNet(webServiceParam);
  }

  public void downloadDictData() {
    Map params = new HashMap();
    params.put("S_PDTG_EMPLOPCODE", "admin");
    params.put("date", "2010");
    String method = "Getdingdan";
    String action = "http://tempuri.org/Getdingdan";
    WebServiceParam webServiceParam =
        new WebServiceParam((Context) downCargoView, params, method, action, dictHandler, 1);
    webServiceConnect.addNet(webServiceParam);
  }
}
