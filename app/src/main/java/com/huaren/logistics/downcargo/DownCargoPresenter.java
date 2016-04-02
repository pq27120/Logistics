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
import com.huaren.logistics.util.DateUtil;
import com.huaren.logistics.util.webservice.WebServiceConnect;
import com.huaren.logistics.util.webservice.WebServiceHandler;
import com.huaren.logistics.util.webservice.WebServiceParam;
import de.greenrobot.dao.query.QueryBuilder;
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
  public WebServiceHandler dictValueHandler;
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

    dictValueHandler = new WebServiceHandler((Context) downCargoView) {
      @Override public void handleFirst() {

      }

      @Override public void handleMsg(int returnCode, Object detail) {
        switch (returnCode) {
          case 1:
            parseDictValueInfo(detail);
            break;
        }
      }
    };
  }

  private void parseDictValueInfo(Object detail) {
    List<SysDicValue> sysDicValueList = null;
    SysDicValue sysDicValue = null;
    SoapObject soapObject = (SoapObject) detail;
    String xml = soapObject.getPropertyAsString("GetDictionaryValueResult");
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
            sysDicValueList = new ArrayList<>();
            break;
          // 开始标记
          case XmlPullParser.START_TAG:
            // 获得当前节点名（标记名）
            String name = parser.getName();
            if ("Data".equals(name)) {
              sysDicValue = new SysDicValue();
            }
            if ("ID".equals(name)) {
              String id = parser.nextText();
              sysDicValue.setId(Long.valueOf(id));
            }
            if ("MyDisplayValue".equals(name)) {
              sysDicValue.setMyDisplayValue(parser.nextText());
            }
            if ("DictionaryTableID".equals(name)) {
              sysDicValue.setDicId(Integer.valueOf(parser.nextText()));
            }
            if ("Note".equals(name)) {
              sysDicValue.setNote(parser.nextText());
            }
            if ("MyValue".equals(name)) {
              sysDicValue.setMyName(parser.nextText());
            }
            break;
          // 结束标记
          case XmlPullParser.END_TAG:
            String endName = parser.getName();
            if ("Data".equals(endName)) {
              sysDicValueList.add(sysDicValue);
              sysDicValue = null;
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
    SysDicValueDao sysDicValueDao = LogisticsApplication.getInstance().getSysDicValueDao();
    sysDicValueDao.insertOrReplaceInTx(sysDicValueList);
  }

  private void parseOrderInfo(Object detail) {
    parseOrderXml(detail);
    String time = DateUtil.parseCurrDateToString("yyyy-MM-dd HH:mm:ss");
    downCargoView.showUpdateView(time, buffer.toString());
  }

  private void parseDictInfo(Object detail) {
    List<SysDic> sysDicList = null;
    SysDic sysDic = null;
    SoapObject soapObject = (SoapObject) detail;
    String xml = soapObject.getPropertyAsString("GetDictionaryTableResult");
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
            sysDicList = new ArrayList<>();
            break;
          // 开始标记
          case XmlPullParser.START_TAG:
            // 获得当前节点名（标记名）
            String name = parser.getName();
            if ("Data".equals(name)) {
              sysDic = new SysDic();
            }
            if ("ID".equals(name)) {
              String id = parser.nextText();
              sysDic.setId(Long.valueOf(id));
            }
            if ("MyName".equals(name)) {
              // 获取当前节点名的文本节点的值
              sysDic.setMyName(parser.nextText());
            }
            if ("MyState".equals(name)) {
              sysDic.setMyState(parser.nextText());
            }
            if ("Note".equals(name)) {
              sysDic.setNote(parser.nextText());
            }
            break;
          // 结束标记
          case XmlPullParser.END_TAG:
            String endName = parser.getName();
            if ("Data".equals(endName)) {
              sysDicList.add(sysDic);
              sysDic = null;
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
    SysDicDao sysDicDao = LogisticsApplication.getInstance().getSysDicDao();
    sysDicDao.insertOrReplaceInTx(sysDicList);
  }

  private void insertCustomer(Map<String, Customer> customerMap) {
    CustomerDao customerDao = LogisticsApplication.getInstance().getCustomerDao();
    int customerNum = 0;
    for (Map.Entry<String, Customer> customerEntry : customerMap.entrySet()) {
      Customer customer = customerEntry.getValue();
      customer.setAddTime(new Date());
      QueryBuilder qb = customerDao.queryBuilder();
      List<Customer> customerList =
          qb.where(CustomerDao.Properties.CooperateId.eq(customer.getCooperateId())).list();
      if (customerList == null || customerList.isEmpty()) {
        customerNum++;
        customerDao.insert(customer);
      }
    }
    buffer.append(customerNum).append("条客户信息");
  }

  private void insertOrderDetail(List<OrderDetail> orderDetailList) {
    OrderDetailDao orderDetailDao = LogisticsApplication.getInstance().getOrderDetailDao();
    int detailNum = 0;
    for (OrderDetail orderDetail : orderDetailList) {
      orderDetail.setAddTime(new Date());
      orderDetail.setDetailStatus("1");
      QueryBuilder qb = orderDetailDao.queryBuilder();
      List<OrderDetail> queryList =
          qb.where(OrderDetailDao.Properties.Lpn.eq(orderDetail.getLpn())).list();
      if (queryList == null || queryList.isEmpty()) {
        detailNum++;
        orderDetailDao.insert(orderDetail);
      }
    }
    buffer.append(",").append(detailNum).append("条订单详情");
  }

  private void insertOrder(List<LogisticsOrder> logisticsOrderList) {
    LogisticsOrderDao logisticsOrderDao = LogisticsApplication.getInstance().getLogisticsOrderDao();
    int orderNum = 0;
    for (LogisticsOrder logisticsOrder : logisticsOrderList) {
      logisticsOrder.setAddTime(new Date());
      logisticsOrder.setOrderStatus("1");
      QueryBuilder qb = logisticsOrderDao.queryBuilder();
      List<LogisticsOrderDao> queryList =
          qb.where(LogisticsOrderDao.Properties.Ordered.eq(logisticsOrder.getOrdered())).list();
      if (queryList == null || queryList.isEmpty()) {
        orderNum++;
        logisticsOrderDao.insert(logisticsOrder);
      }
    }
    buffer.append(",").append(orderNum).append("条订单");
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
    String method = "GetDictionaryTable";
    String action = "http://tempuri.org/GetDictionaryTable";
    WebServiceParam webServiceParam =
        new WebServiceParam((Context) downCargoView, params, method, action, dictHandler, 1);
    webServiceConnect.addNet(webServiceParam);
  }

  public void downloadDictValueData() {
    Map params = new HashMap();
    params.put("S_PDTG_EMPLOPCODE", "admin");
    params.put("date", "2010");
    String method = "GetDictionaryValue";
    String action = "http://tempuri.org/GetDictionaryValue";
    WebServiceParam webServiceParam =
        new WebServiceParam((Context) downCargoView, params, method, action, dictValueHandler, 1);
    webServiceConnect.addNet(webServiceParam);
  }
}
