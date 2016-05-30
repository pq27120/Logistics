package com.huaren.logistics.downcargo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
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
import com.huaren.logistics.util.CommonTool;
import com.huaren.logistics.util.DateUtil;
import com.huaren.logistics.util.UiTool;
import com.huaren.logistics.util.http.NetConnect;
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

import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.QueryBuilder;

public class DownCargoPresenter {

    private IDownCargoView downCargoView;

    public WebServiceHandler handler;
    public WebServiceHandler dictHandler;
    public WebServiceHandler dictValueHandler;
    public WebServiceHandler delHandler;
    protected WebServiceConnect webServiceConnect = new WebServiceConnect();
    private String customerStr = "";
    private String detailStr = "";
    private boolean isFinishOne = false;
    private boolean isFinishTwo = false;
    private boolean isFinishThree = false;

    public DownCargoPresenter(final IDownCargoView downCargoView) {
        this.downCargoView = downCargoView;

        handler = new WebServiceHandler((Context) downCargoView) {
            @Override
            public void handleFirst() {

            }

            @Override
            public void handleMsg(int returnCode, Object detail) {
                switch (returnCode) {
                    case NetConnect.NET_ERROR:
                        downCargoView.finishActivity();
                        break;
                    case 1:
                        CommonTool.showLog("获取订单" + DateUtil.parseDateToString(new Date(), DateUtil.DATE_TIME_FORMATE));
                        parseOrderInfo(detail);
                        break;
                }
            }
        };

        dictHandler = new WebServiceHandler((Context) downCargoView) {
            @Override
            public void handleFirst() {

            }

            @Override
            public void handleMsg(int returnCode, Object detail) {
                switch (returnCode) {
                    case NetConnect.NET_ERROR:
                        downCargoView.finishActivity();
                        break;
                    case 1:
                        parseDictInfo(detail);
                        break;
                }
            }
        };

        dictValueHandler = new WebServiceHandler((Context) downCargoView) {
            @Override
            public void handleFirst() {

            }

            @Override
            public void handleMsg(int returnCode, Object detail) {
                switch (returnCode) {
                    case NetConnect.NET_ERROR:
                        downCargoView.finishActivity();
                        break;
                    case 1:
                        parseDictValueInfo(detail);
                        break;
                }
            }
        };

        delHandler = new WebServiceHandler((Context) downCargoView) {
            @Override
            public void handleFirst() {

            }

            @Override
            public void handleMsg(int returnCode, Object detail) {
                switch (returnCode) {
                    case NetConnect.NET_ERROR:
                        downCargoView.finishActivity();
                        break;
                    case 1:
                        parseDelOrderInfo(detail);
                        break;
                }
            }
        };
    }

    /**
     * 用Handler来更新UI
     */
    private Handler finishHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    isFinishOne = true;
                    break;
                case 2:
                    isFinishTwo = true;
                    downloadDelOrderData();
                    break;
                case 3:
                    isFinishThree = true;
                    break;
            }
            if (isFinishOne && isFinishTwo && isFinishThree) {
                String time = DateUtil.parseCurrDateToString("yyyy-MM-dd HH:mm:ss");
                downCargoView.showUpdateView(time, "更新完成，更新了" + customerStr + "," + detailStr);
            }
        }
    };

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
//        SoapObject soapObject = (SoapObject) detail;
//        UiTool.showToast((Context) downCargoView, "报文返回" + soapObject.toString());
        try {
            parseOrderXml(detail);
        } catch (Exception e) {
            e.printStackTrace();
//            UiTool.showToast((Context) downCargoView, CommonTool.sbCrashInfo2Str((Context)downCargoView, e).toString());
            downCargoView.finishActivity();
        }
    }

    private void parseDelOrderInfo(Object detail) {
        parseDelOrderXml(detail);
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
        List<Customer> insertCustomerList = new ArrayList<>();
        CustomerDao customerDao = LogisticsApplication.getInstance().getCustomerDao();
        List<Customer> existCustomerList =
                customerDao.queryBuilder().where(CustomerDao.Properties.Status.eq("1")).list();
        int customerNum = 0;
        for (Customer customer : customerList) {
            boolean existFlag = false;
            for (int i = 0; i < existCustomerList.size(); i++) {
                Customer existCustomer = existCustomerList.get(i);
                if (customer.getCooperateId().equals(existCustomer.getCooperateId())
                        && customer.getLPdtgBatch() == existCustomer.getLPdtgBatch()) {
                    existFlag = true;
                }
            }
            if (!existFlag) {
                customerNum++;
                String userName = CommonTool.getSharePreference((Context) downCargoView, "curUserName");
                customer.setUserName(userName);
                customer.setId(customer.getCooperateId() + customer.getLPdtgBatch());
                customer.setAddTime(new Date());
                customer.setStatus("1");
                insertCustomerList.add(customer);
            }
        }
        customerDao.insertInTx(insertCustomerList);
        insertCustomerList.clear();
        CommonTool.showLog(customerNum + "条客户信息");
        customerStr = customerNum + "条客户信息";
    }

    private void insertOrderDetail(List<OrderDetail> orderDetailList) {
        List<OrderDetail> insertDetailList = new ArrayList<>();
        OrderDetailDao orderDetailDao = LogisticsApplication.getInstance().getOrderDetailDao();
        List<OrderDetail> existDetailList =
                orderDetailDao.queryBuilder().where(OrderDetailDao.Properties.Status.eq("1")).list();
        int detailCount = 0;
        for (OrderDetail orderDetail : orderDetailList) {
            boolean existFlag = false;
            for (int i = 0; i < existDetailList.size(); i++) {
                OrderDetail existOrderDetail = existDetailList.get(i);
                if (orderDetail.getDetailId().equals(existOrderDetail.getDetailId())) {
                    existFlag = true;
                }
            }
            if (!existFlag) {
                detailCount++;
                String userName = CommonTool.getSharePreference((Context) downCargoView, "curUserName");
                orderDetail.setUserName(userName);
                orderDetail.setAddTime(new Date());
                orderDetail.setStatus("1");
                orderDetail.setDetailStatus(OrderStatusEnum.READEY_CARGO.getStatus());
                orderDetail.setCustomerId(orderDetail.getCooperateId() + orderDetail.getLPdtgBatch());
                insertDetailList.add(orderDetail);
            }
        }
        orderDetailDao.insertInTx(insertDetailList);
        insertDetailList.clear();
        CommonTool.showLog(detailCount + "条明细");
        detailStr = detailCount + "条明细";
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

    private void parseDelOrderXml(Object detail) {
        //批量信息
        SoapObject soapObject = (SoapObject) detail;
        String xml = soapObject.getPropertyAsString("GetDeleteDataResult");
        List<OrderDetail> orderDetailList = parseDelOrderDetail(xml);
        delOrderDetail(orderDetailList);
    }

    /**
     * 插入批量订单信息
     */
    private void insertOrderBatch(List<OrderBatch> orderBatchList) {
        List<OrderBatch> insertOrderBatchList = new ArrayList<>();
        OrderBatchDao dao = LogisticsApplication.getInstance().getOrderBatchDao();
        List<OrderBatch> existOrderBatchList = dao.loadAll();
        for (int i = 0; i < orderBatchList.size(); i++) {
            OrderBatch orderBatch = orderBatchList.get(i);
            orderBatch.setId(
                    orderBatch.getCooperateId() + orderBatch.getLPdtgBatch() + orderBatch.getDriversID());
            boolean existFlag = false;
            for (int j = 0; j < existOrderBatchList.size(); j++) {
                OrderBatch existOrderBatch = existOrderBatchList.get(j);
                if (orderBatch.getId().equals(existOrderBatch.getId())) {
                    existFlag = true;
                    orderBatch.setCanEvalutaion(existOrderBatch.getCanEvalutaion());
                    orderBatch.setEvaluation(existOrderBatch.getEvaluation());
                } else {
                    orderBatch.setCanEvalutaion("0");
                }
            }
            if (!existFlag) {
                String userName = CommonTool.getSharePreference((Context) downCargoView, "curUserName");
                orderBatch.setUserName(userName);
                insertOrderBatchList.add(orderBatch);
            }
        }
        dao.insertInTx(insertOrderBatchList);
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
        finishHandler.sendEmptyMessage(2);
        return orderDetailList;
    }

    /**
     * 解析删除明细
     */
    private List<OrderDetail> parseDelOrderDetail(String xml) {
        List<OrderDetail> orderDetailList = null;
        OrderDetail orderDetail = null;
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
                        if ("Item".equals(name)) {
                            orderDetail = new OrderDetail();
                        }
                        if ("DetailID".equals(name)) {
                            orderDetail.setDetailId(parser.nextText());
                        }
                        break;
                    // 结束标记
                    case XmlPullParser.END_TAG:
                        String endName = parser.getName();
                        if ("Item".equals(endName)) {
                            orderDetailList.add(orderDetail);
                            orderDetail = null;
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
        finishHandler.sendEmptyMessage(3);
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
        String userName = CommonTool.getSharePreference((Context) downCargoView, "curUserName");
        List<DownBatchInfo> downBatchInfoList = downBatchInfoDao.queryBuilder().where(DownBatchInfoDao.Properties.UserName.eq(userName)).list();
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

        if (lPdtgBatch > 0) {
            if (downBatchInfoList == null || downBatchInfoList.isEmpty()) {
                DownBatchInfo downBatchInfo = new DownBatchInfo();
                downBatchInfo.setLPdtgBatch(lPdtgBatch);
                downBatchInfo.setAddTime(new Date());
                downBatchInfo.setUserName(userName);
                downBatchInfoDao.insert(downBatchInfo);
            } else {
                DownBatchInfo downBatchInfo = downBatchInfoList.get(0);
                if (lPdtgBatch > downBatchInfo.getLPdtgBatch()) {
//                updateCustomerStatus(downBatchInfo.getLPdtgBatch(), userName);
                    delCustomer(downBatchInfo.getLPdtgBatch(), userName);
//                updateOrderBatchStatus(downBatchInfo.getLPdtgBatch(), userName);
                    delOrderBatch(downBatchInfo.getLPdtgBatch(), userName);
//                updateOrderDetailStatus(downBatchInfo.getLPdtgBatch(), userName);
                    delOrderDetail(downBatchInfo.getLPdtgBatch(), userName);
                    updateRecycleInputStatus(downBatchInfo.getLPdtgBatch(), userName);
                    updateRecycleScanStatus(downBatchInfo.getLPdtgBatch(), userName);
                    updateDownBatchInfo(lPdtgBatch, userName);
                }
            }
        }
    }

    /**
     * 删除下载批次记录
     */

    private void updateDownBatchInfo(long newlPdtgBatch, String userName) {
        DownBatchInfoDao dao = LogisticsApplication.getInstance().getDownBatchInfoDao();
        QueryBuilder<DownBatchInfo> qb = dao.queryBuilder();
        DeleteQuery<DownBatchInfo> bd = qb.where(DownBatchInfoDao.Properties.UserName.eq(userName)).buildDelete();
        bd.executeDeleteWithoutDetachingEntities();
        DownBatchInfo downBatchInfo = new DownBatchInfo();
        downBatchInfo.setLPdtgBatch(newlPdtgBatch);
        downBatchInfo.setAddTime(new Date());
        downBatchInfo.setUserName(userName);
        dao.insert(downBatchInfo);
    }

    /**
     * 根据批次号隐藏回收扫描信息
     */
    private void updateRecycleScanStatus(long lPdtgBatch, String userName) {
        RecycleScanDao dao = LogisticsApplication.getInstance().getRecycleScanDao();
        List<RecycleScan> recycleScanList =
                dao.queryBuilder().where(RecycleScanDao.Properties.LPdtgBatch.eq(lPdtgBatch), RecycleScanDao.Properties.UserName.eq(userName)).list();
        for (int i = 0; i < recycleScanList.size(); i++) {
            RecycleScan recycleScan = recycleScanList.get(i);
            recycleScan.setStatus("0");
        }
        dao.updateInTx(recycleScanList);
    }

    /**
     * 根据批次号隐藏回收录入信息
     */
    private void updateRecycleInputStatus(long lPdtgBatch, String userName) {
        RecycleInputDao dao = LogisticsApplication.getInstance().getRecycleInputDao();
        List<RecycleInput> recycleInputList =
                dao.queryBuilder().where(RecycleInputDao.Properties.LPdtgBatch.eq(lPdtgBatch), RecycleInputDao.Properties.UserName.eq(userName)).list();
        for (int i = 0; i < recycleInputList.size(); i++) {
            RecycleInput recycleInput = recycleInputList.get(i);
            recycleInput.setStatus("0");
        }
        dao.updateInTx(recycleInputList);
    }

    /**
     * 根据批次号隐藏订单详情
     */
    private void updateOrderDetailStatus(long lPdtgBatch, String userName) {
        OrderDetailDao dao = LogisticsApplication.getInstance().getOrderDetailDao();
        List<OrderDetail> orderDetailList =
                dao.queryBuilder().where(OrderDetailDao.Properties.LPdtgBatch.eq(lPdtgBatch), OrderDetailDao.Properties.UserName.eq(userName)).list();
        for (int i = 0; i < orderDetailList.size(); i++) {
            OrderDetail orderDetail = orderDetailList.get(i);
            orderDetail.setStatus("0");
        }
        dao.updateInTx(orderDetailList);
    }

    /**
     * 根据批次号删除订单详情
     */
    private void delOrderDetail(long lPdtgBatch, String userName) {
        OrderDetailDao dao = LogisticsApplication.getInstance().getOrderDetailDao();
        QueryBuilder<OrderDetail> qb = dao.queryBuilder();
        DeleteQuery<OrderDetail> bd = qb.where(OrderDetailDao.Properties.LPdtgBatch.eq(lPdtgBatch), OrderDetailDao.Properties.UserName.eq(userName)).buildDelete();
        bd.executeDeleteWithoutDetachingEntities();
    }

    /**
     * 根据批次号删除订单详情
     */
    private void delOrderDetail(List<OrderDetail> orderDetailList) {
        //TODO
//        OrderDetail delO = new OrderDetail();
//        delO.setDetailId("94348");
//        orderDetailList.add(delO);
//
//        delO = new OrderDetail();
//        delO.setDetailId("94680");
//        orderDetailList.add(delO);
        OrderDetailDao dao = LogisticsApplication.getInstance().getOrderDetailDao();
        for (int i = 0; i < orderDetailList.size(); i++) {
            OrderDetail orderDetail = orderDetailList.get(i);
            QueryBuilder<OrderDetail> qb = dao.queryBuilder();
            DeleteQuery<OrderDetail> bd = qb.where(OrderDetailDao.Properties.DetailId.eq(orderDetail.getDetailId())).buildDelete();
            bd.executeDeleteWithoutDetachingEntities();
        }
    }

    /**
     * 根据批次号隐藏订单批次
     */
    private void updateOrderBatchStatus(long lPdtgBatch, String userName) {
        OrderBatchDao dao = LogisticsApplication.getInstance().getOrderBatchDao();
        List<OrderBatch> orderBatchList =
                dao.queryBuilder().where(OrderBatchDao.Properties.LPdtgBatch.eq(lPdtgBatch), OrderBatchDao.Properties.UserName.eq(userName)).list();
        for (int i = 0; i < orderBatchList.size(); i++) {
            OrderBatch orderBatch = orderBatchList.get(i);
            orderBatch.setStatus("0");
        }
        dao.updateInTx(orderBatchList);
    }

    /**
     * 根据批次号删除订单批次
     */
    private void delOrderBatch(long lPdtgBatch, String userName) {
        OrderBatchDao dao = LogisticsApplication.getInstance().getOrderBatchDao();
        QueryBuilder<OrderBatch> qb = dao.queryBuilder();
        DeleteQuery<OrderBatch> bd = qb.where(OrderBatchDao.Properties.LPdtgBatch.eq(lPdtgBatch), OrderBatchDao.Properties.UserName.eq(userName)).buildDelete();
        bd.executeDeleteWithoutDetachingEntities();

    }

    /**
     * 根据批次号隐藏客户信息
     */
    private void updateCustomerStatus(long lPdtgBatch, String userName) {
        CustomerDao dao = LogisticsApplication.getInstance().getCustomerDao();
        List<Customer> customerList =
                dao.queryBuilder().where(CustomerDao.Properties.LPdtgBatch.eq(lPdtgBatch), CustomerDao.Properties.UserName.eq(userName)).list();
        for (int i = 0; i < customerList.size(); i++) {
            Customer customer = customerList.get(i);
            customer.setStatus("0");
        }
        dao.updateInTx(customerList);
    }

    /**
     * 根据批次号删除客户信息
     */
    private void delCustomer(long lPdtgBatch, String userName) {
        CustomerDao dao = LogisticsApplication.getInstance().getCustomerDao();
        QueryBuilder<Customer> qb = dao.queryBuilder();
        DeleteQuery<Customer> bd = qb.where(CustomerDao.Properties.LPdtgBatch.eq(lPdtgBatch), CustomerDao.Properties.UserName.eq(userName)).buildDelete();
        bd.executeDeleteWithoutDetachingEntities();
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
        finishHandler.sendEmptyMessage(1);
        return customerList;
    }

    public void downloadOrderData() {
        CommonTool.showLog("获取订单开始 " + DateUtil.parseDateToString(new Date(), DateUtil.DATE_TIME_FORMATE));
        Map params = new HashMap();
        String driverId = CommonTool.getSharePreference((Context) downCargoView, "driverId");
        CommonTool.showLog("司机ID " + driverId);
        params.put("parDriversID", driverId);
        String method = "Getdingdan";
        String action = "http://tempuri.org/Getdingdan";
        WebServiceParam webServiceParam =
                new WebServiceParam((Context) downCargoView, params, method, action, handler, 1);
        webServiceConnect.addNet(webServiceParam);
    }

    public void downloadDictData() {
        Map params = new HashMap();
        String method = "GetDictionaryTable";
        String action = "http://tempuri.org/GetDictionaryTable";
        WebServiceParam webServiceParam =
                new WebServiceParam((Context) downCargoView, params, method, action, dictHandler, 1);
        webServiceConnect.addNet(webServiceParam);
    }

    public void downloadDictValueData() {
        Map params = new HashMap();
        String method = "GetDictionaryValue";
        String action = "http://tempuri.org/GetDictionaryValue";
        WebServiceParam webServiceParam =
                new WebServiceParam((Context) downCargoView, params, method, action, dictValueHandler, 1);
        webServiceConnect.addNet(webServiceParam);
    }

    public void downloadDelOrderData() {
        Map params = new HashMap();
        String method = "GetDeleteData ";
        String action = "http://tempuri.org/GetDeleteData ";
        String driverId = CommonTool.getSharePreference((Context) downCargoView, "driverId");
        params.put("parDriversID", driverId);
        WebServiceParam webServiceParam =
                new WebServiceParam((Context) downCargoView, params, method, action, delHandler, 1);
        webServiceConnect.addNet(webServiceParam);
    }
}
