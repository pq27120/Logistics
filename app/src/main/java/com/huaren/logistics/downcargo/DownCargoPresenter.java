package com.huaren.logistics.downcargo;

import android.content.Context;
import android.util.Xml;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.bean.Customer;
import com.huaren.logistics.bean.DownBatchInfo;
import com.huaren.logistics.bean.OrderBatch;
import com.huaren.logistics.bean.OrderDetail;
import com.huaren.logistics.bean.RecycleInput;
import com.huaren.logistics.bean.RecycleScan;
import com.huaren.logistics.bean.SysDic;
import com.huaren.logistics.bean.SysDicValue;
import com.huaren.logistics.common.OrderStatusEnum;
import com.huaren.logistics.dao.CustomerDao;
import com.huaren.logistics.dao.DownBatchInfoDao;
import com.huaren.logistics.dao.OrderBatchDao;
import com.huaren.logistics.dao.OrderDetailDao;
import com.huaren.logistics.dao.RecycleInputDao;
import com.huaren.logistics.dao.RecycleScanDao;
import com.huaren.logistics.dao.SysDicDao;
import com.huaren.logistics.dao.SysDicValueDao;
import com.huaren.logistics.util.DateUtil;
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

  private void insertCustomer(List<Customer> customerList) {
    CustomerDao customerDao = LogisticsApplication.getInstance().getCustomerDao();
    int customerNum = customerList.size();
    for (int i = 0; i < customerList.size(); i++) {
      Customer customer = customerList.get(i);
      customer.setId(customer.getCooperateId() + customer.getLPdtgBatch());
      customerDao.insertOrReplace(customer);
    }
    buffer.append(customerNum).append("条客户信息");
  }

  private void insertOrderDetail(List<OrderDetail> orderDetailList) {
    OrderDetailDao orderDetailDao = LogisticsApplication.getInstance().getOrderDetailDao();
    for (OrderDetail orderDetail : orderDetailList) {
      OrderDetail existDetail = orderDetailDao.queryBuilder()
          .where(OrderDetailDao.Properties.DetailId.eq(orderDetail.getDetailId()))
          .unique();
      if (existDetail != null) {
        orderDetail.setStatus(existDetail.getStatus());
        orderDetail.setDetailStatus(existDetail.getDetailStatus());
        orderDetail.setCustomerId(existDetail.getCustomerId());
        orderDetail.setEditTime(new Date());
        orderDetail.setAddTime(existDetail.getAddTime());
      } else {
        orderDetail.setAddTime(new Date());
        orderDetail.setStatus("1");
        orderDetail.setDetailStatus(OrderStatusEnum.READEY_CARGO.getStatus());
        orderDetail.setCustomerId(orderDetail.getCooperateId() + orderDetail.getLPdtgBatch());
      }
      orderDetailDao.insertOrReplace(orderDetail);
    }
    buffer.append(",").append(orderDetailList.size()).append("条明细");
  }

  private void parseOrderXml(Object detail) {
    //批量信息
    SoapObject soapObject = (SoapObject) detail;
    String xml = soapObject.getPropertyAsString("GetdingdanResult");
    parseBatchOrder(xml);
    List<Customer> customerList = parseCustomer(xml);
    List<OrderBatch> orderBatchList = parseOrderBatch(xml);
    List<OrderDetail> orderDetailList = parseOrderDetail(xml);
    insertCustomer(customerList);
    insertOrderBatch(orderBatchList);
    insertOrderDetail(orderDetailList);
  }

  /**
   * 插入批量订单信息
   */
  private void insertOrderBatch(List<OrderBatch> orderBatchList) {
    OrderBatchDao dao = LogisticsApplication.getInstance().getOrderBatchDao();
    for (int i = 0; i < orderBatchList.size(); i++) {
      OrderBatch orderBatch = orderBatchList.get(i);
      orderBatch.setId(
          orderBatch.getCooperateId() + orderBatch.getLPdtgBatch() + orderBatch.getDriversID());
      OrderBatch existOrderBatch = dao.queryBuilder()
          .where(OrderBatchDao.Properties.Id.eq(
              orderBatch.getCooperateId() + orderBatch.getLPdtgBatch() + orderBatch.getDriversID()))
          .unique();
      if (existOrderBatch != null) {
        orderBatch.setCanEvalutaion(existOrderBatch.getCanEvalutaion());
        orderBatch.setEvaluation(existOrderBatch.getEvaluation());
      } else {
        orderBatch.setCanEvalutaion("0");
      }
      dao.insertOrReplace(orderBatch);
    }
  }

  /**
   * 解析明细
   */
  private List<OrderDetail> parseOrderDetail(String xml) {
    List<OrderDetail> orderDetailList = null;
    OrderDetail orderDetail = null;
    String cooperateId = null;
    String dispatchCreatTime = null;
    String driversID = null;
    String sPdtgEmplname = null;
    String sPdtgEmplname2 = null;
    String suicherenyuanID = null;
    String suicherenyuanID2 = null;
    String sPdtgEmplname3 = null;
    String sPdtgVehicleno = null;
    String countPieces = null;
    int lPdtgBatch = 0;
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
              orderDetailList = new ArrayList<>();
            }
            if ("ItemHeader".equals(name)) {
              cooperateId = "";
              dispatchCreatTime = "";
              driversID = "";
              sPdtgEmplname = "";
              sPdtgEmplname2 = "";
              suicherenyuanID = "";
              suicherenyuanID2 = "";
              sPdtgEmplname3 = "";
              sPdtgVehicleno = "";
              countPieces = "";
              lPdtgBatch = 0;
            }
            if ("CooperateID".equals(name)) {
              cooperateId = parser.nextText();
            }
            if ("DispatchCreatTime".equals(name)) {
              dispatchCreatTime = parser.nextText();
            }
            if ("DriversID".equals(name)) {
              driversID = parser.nextText();
            }
            if ("S_PDTG_EMPLNAME".equals(name)) {
              sPdtgEmplname = parser.nextText();
            }
            if ("S_PDTG_EMPLNAME2".equals(name)) {
              sPdtgEmplname2 = parser.nextText();
            }
            if ("suicherenyuanID".equals(name)) {
              suicherenyuanID = parser.nextText();
            }
            if ("suicherenyuanID2".equals(name)) {
              suicherenyuanID2 = parser.nextText();
            }
            if ("S_PDTG_EMPLNAME3".equals(name)) {
              sPdtgEmplname3 = parser.nextText();
            }
            if ("S_Pdtg_Vehicleno".equals(name)) {
              sPdtgVehicleno = parser.nextText();
            }
            if ("CountPieces".equals(name)) {
              countPieces = parser.nextText();
            }
            if ("L_PDTG_BATCH".equals(name)) {
              lPdtgBatch = Integer.valueOf(parser.nextText());
            }
            if ("Item".equals(name)) {
              orderDetail = new OrderDetail();
              orderDetail.setCooperateId(cooperateId);
              orderDetail.setDispatchCreatTime(dispatchCreatTime);
              orderDetail.setDriversID(driversID);
              orderDetail.setSPdtgEmplname(sPdtgEmplname);
              orderDetail.setSPdtgEmplname2(sPdtgEmplname2);
              orderDetail.setSuicherenyuanID(suicherenyuanID);
              orderDetail.setSuicherenyuanID2(suicherenyuanID2);
              orderDetail.setSPdtgEmplname3(sPdtgEmplname3);
              orderDetail.setSPdtgVehicleno(sPdtgVehicleno);
              orderDetail.setCountPieces(countPieces);
              orderDetail.setLPdtgBatch(lPdtgBatch);
            }
            if ("DispatchNumber".equals(name)) {
              orderDetail.setDispatchNumber(parser.nextText());
            }
            if ("OrderID".equals(name)) {
              orderDetail.setOrderId(parser.nextText());
            }
            if ("WAVEKEY".equals(name)) {
              orderDetail.setWaveKey(parser.nextText());
            }
            if ("DetailID".equals(name)) {
              orderDetail.setDetailId(parser.nextText());
            }
            if ("ORDERED".equals(name)) {
              orderDetail.setOrdered(parser.nextText());
            }
            if ("MTYPE".equals(name)) {
              orderDetail.setMtype(parser.nextText());
            }
            if ("LPN".equals(name)) {
              orderDetail.setLpn(parser.nextText());
            }
            if ("UOM".equals(name)) {
              orderDetail.setUom(parser.nextText());
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
              cooperateId = null;
              dispatchCreatTime = null;
              driversID = null;
              sPdtgEmplname = null;
              sPdtgEmplname2 = null;
              suicherenyuanID = null;
              suicherenyuanID2 = null;
              sPdtgEmplname3 = null;
              sPdtgVehicleno = null;
              countPieces = null;
              lPdtgBatch = 0;
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
    return orderDetailList;
  }

  /**
   * 解析批次、司机、客户
   */
  private List<OrderBatch> parseOrderBatch(String xml) {
    List<OrderBatch> orderBatchList = null;
    OrderBatch orderBatch = null;
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
              orderBatchList = new ArrayList<>();
            }
            if ("ItemHeader".equals(name)) {
              orderBatch = new OrderBatch();
              orderBatch.setAddTime(new Date());
              orderBatch.setStatus("1");
            }
            if ("CooperateID".equals(name)) {
              orderBatch.setCooperateId(parser.nextText());
            }
            if ("DriversID".equals(name)) {
              orderBatch.setDriversID(parser.nextText());
            }
            if ("L_PDTG_BATCH".equals(name)) {
              orderBatch.setLPdtgBatch(Integer.valueOf(parser.nextText()));
            }
            if ("S_PDTG_CUSTFULLNAME".equals(name)) {
              orderBatch.setSPdtgCustfullname(parser.nextText());
            }
            break;
          // 结束标记
          case XmlPullParser.END_TAG:
            String endName = parser.getName();
            if ("ItemHeader".equals(endName)) {
              orderBatchList.add(orderBatch);
              orderBatch = null;
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
    return orderBatchList;
  }

  /**
   * 解析批量数据
   */
  private void parseBatchOrder(String xml) {
    DownBatchInfoDao downBatchInfoDao = LogisticsApplication.getInstance().getDownBatchInfoDao();
    List<DownBatchInfo> downBatchInfoList = downBatchInfoDao.queryBuilder().list();
    long lPdtgBatch = 0;
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
            if ("L_PDTG_BATCH".equals(name)) {
              lPdtgBatch = Long.valueOf(parser.nextText());
            }
        }
        // 获得解析器中的下一个事件
        eventType = parser.next();
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (XmlPullParserException e) {
      e.printStackTrace();
    }

    if (downBatchInfoList == null || downBatchInfoList.isEmpty()) {
      DownBatchInfo downBatchInfo = new DownBatchInfo();
      downBatchInfo.setLPdtgBatch(lPdtgBatch);
      downBatchInfo.setAddTime(new Date());
      downBatchInfoDao.insert(downBatchInfo);
    } else {
      DownBatchInfo downBatchInfo = downBatchInfoList.get(0);
      if (lPdtgBatch > downBatchInfo.getLPdtgBatch()) {
        updateCustomerStatus(downBatchInfo.getLPdtgBatch());
        updateOrderBatchStatus(downBatchInfo.getLPdtgBatch());
        updateOrderDetailStatus(downBatchInfo.getLPdtgBatch());
        updateRecycleInputStatus(downBatchInfo.getLPdtgBatch());
        updateRecycleScanStatus(downBatchInfo.getLPdtgBatch());
        updateDownBatchInfo(lPdtgBatch);
      }
    }
  }

  /**
   * 删除下载批次记录
   */

  private void updateDownBatchInfo(long newlPdtgBatch) {
    DownBatchInfoDao dao = LogisticsApplication.getInstance().getDownBatchInfoDao();
    dao.deleteAll();
    DownBatchInfo downBatchInfo = new DownBatchInfo();
    downBatchInfo.setLPdtgBatch(newlPdtgBatch);
    downBatchInfo.setAddTime(new Date());
    dao.insert(downBatchInfo);
  }

  /**
   * 根据批次号隐藏回收扫描信息
   */
  private void updateRecycleScanStatus(long lPdtgBatch) {
    RecycleScanDao dao = LogisticsApplication.getInstance().getRecycleScanDao();
    List<RecycleScan> recycleScanList =
        dao.queryBuilder().where(RecycleScanDao.Properties.LPdtgBatch.eq(lPdtgBatch)).list();
    for (int i = 0; i < recycleScanList.size(); i++) {
      RecycleScan recycleScan = recycleScanList.get(i);
      recycleScan.setStatus("0");
    }
    dao.updateInTx(recycleScanList);
  }

  /**
   * 根据批次号隐藏回收录入信息
   */
  private void updateRecycleInputStatus(long lPdtgBatch) {
    RecycleInputDao dao = LogisticsApplication.getInstance().getRecycleInputDao();
    List<RecycleInput> recycleInputList =
        dao.queryBuilder().where(RecycleInputDao.Properties.LPdtgBatch.eq(lPdtgBatch)).list();
    for (int i = 0; i < recycleInputList.size(); i++) {
      RecycleInput recycleInput = recycleInputList.get(i);
      recycleInput.setStatus("0");
    }
    dao.updateInTx(recycleInputList);
  }

  /**
   * 根据批次号隐藏订单详情
   */
  private void updateOrderDetailStatus(long lPdtgBatch) {
    OrderDetailDao dao = LogisticsApplication.getInstance().getOrderDetailDao();
    List<OrderDetail> orderDetailList =
        dao.queryBuilder().where(OrderDetailDao.Properties.LPdtgBatch.eq(lPdtgBatch)).list();
    for (int i = 0; i < orderDetailList.size(); i++) {
      OrderDetail orderDetail = orderDetailList.get(i);
      orderDetail.setStatus("0");
    }
    dao.updateInTx(orderDetailList);
  }

  /**
   * 根据批次号隐藏订单批次
   */
  private void updateOrderBatchStatus(long lPdtgBatch) {
    OrderBatchDao dao = LogisticsApplication.getInstance().getOrderBatchDao();
    List<OrderBatch> orderBatchList =
        dao.queryBuilder().where(OrderBatchDao.Properties.LPdtgBatch.eq(lPdtgBatch)).list();
    for (int i = 0; i < orderBatchList.size(); i++) {
      OrderBatch orderBatch = orderBatchList.get(i);
      orderBatch.setStatus("0");
    }
    dao.updateInTx(orderBatchList);
  }

  /**
   * 根据批次号隐藏客户信息
   */
  private void updateCustomerStatus(long lPdtgBatch) {
    CustomerDao dao = LogisticsApplication.getInstance().getCustomerDao();
    List<Customer> customerList =
        dao.queryBuilder().where(CustomerDao.Properties.LPdtgBatch.eq(lPdtgBatch)).list();
    for (int i = 0; i < customerList.size(); i++) {
      Customer customer = customerList.get(i);
      customer.setStatus("0");
    }
    dao.updateInTx(customerList);
  }

  /**
   * 解析客户数据
   */
  private List<Customer> parseCustomer(String xml) {
    List<Customer> customerList = null;
    Customer customer = null;
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
              customerList = new ArrayList<>();
            }
            if ("ItemHeader".equals(name)) {
              customer = new Customer();
              customer.setAddTime(new Date());
              customer.setStatus("1");
            }
            if ("CooperateID".equals(name)) {
              String cooperateId = parser.nextText();
              customer.setCooperateId(cooperateId);
            }
            if ("CoopPWD".equals(name)) {
              customer.setCoopPwd(parser.nextText());
            }
            if ("S_PDTG_CUSTFULLNAME".equals(name)) {
              customer.setSPdtgCustfullname(parser.nextText());
            }
            if ("L_PDTG_BATCH".equals(name)) {
              customer.setLPdtgBatch(Integer.valueOf(parser.nextText()));
            }
            break;
          // 结束标记
          case XmlPullParser.END_TAG:
            String endName = parser.getName();
            if ("ItemHeader".equals(endName)) {
              customerList.add(customer);
              customer = null;
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
    return customerList;
  }

  public void downloadOrderData() {
    Map params = new HashMap();
    params.put("parDriversID", "341");
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
