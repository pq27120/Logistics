package com.huaren.logistics.uploadcargo;

import android.content.Context;
import android.util.Xml;

import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.bean.OperatorLog;
import com.huaren.logistics.dao.OperatorLogDao;
import com.huaren.logistics.util.CommonTool;
import com.huaren.logistics.util.DateUtil;
import com.huaren.logistics.util.webservice.WebServiceConnect;
import com.huaren.logistics.util.webservice.WebServiceHandler;
import com.huaren.logistics.util.webservice.WebServiceParam;

import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadCargoPresenter {

    private IUploadCargoView uploadCargoView;

    private List<OperatorLog> recordOperatorLogList;
    private List<OperatorLog> evaOperatorLogList;

    public WebServiceHandler handler;
    public WebServiceHandler evaHandler;
    protected WebServiceConnect webServiceConnect = new WebServiceConnect();
    private StringBuffer buffer = new StringBuffer("上传完成,");

    public UploadCargoPresenter(final IUploadCargoView uploadCargoView) {
        this.uploadCargoView = uploadCargoView;

        handler = new WebServiceHandler((Context) uploadCargoView) {
            @Override
            public void handleFirst() {

            }

            @Override
            public void handleMsg(int returnCode, Object detail) {
                switch (returnCode) {
                    case 1:
                        parseRecordInfo(detail);
                        break;
                }
            }
        };

        evaHandler = new WebServiceHandler((Context) uploadCargoView) {
            @Override
            public void handleFirst() {

            }

            @Override
            public void handleMsg(int returnCode, Object detail) {
                switch (returnCode) {
                    case 1:
                        parseEvaRecordInfo(detail);
                        break;
                }
            }
        };
    }

    private void parseRecordInfo(Object detail) {
        parseRecordXml(detail);
    }

    private void parseEvaRecordInfo(Object detail) {
        parseEvaRecordXml(detail);
        String time = DateUtil.parseCurrDateToString("yyyy-MM-dd HH:mm:ss");
        uploadCargoView.showUpdateView(time, buffer.toString());
    }

    private void parseRecordXml(Object detail) {
        //批量信息
        SoapObject soapObject = (SoapObject) detail;
        String xml = soapObject.getPropertyAsString("UploadScanRecordResult");
        boolean flag = false;
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
                        if ("CommandResult".equals(name)) {
                            flag = false;
                        }
                        if ("Code".equals(name)) {
                            String code = parser.nextText();
                            if("0".equals(code)) {
                                flag = true;
                            }
                        }

                        break;
                    // 结束标记
                    case XmlPullParser.END_TAG:
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
        if (flag) {
            OperatorLogDao operatorLogDao = LogisticsApplication.getInstance().getOperatorLogDao();
            operatorLogDao.deleteInTx(recordOperatorLogList);
        }
    }

    private void parseEvaRecordXml(Object detail) {
        //批量信息
        SoapObject soapObject = (SoapObject) detail;
        String xml = soapObject.getPropertyAsString("Upload_kehupingjiaResult");
        boolean flag = false;
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
                        if ("CommandResult".equals(name)) {
                            flag = false;
                        }
                        if ("Code".equals(name)) {
                            String code = parser.nextText();
                            if("0".equals(code)) {
                                flag = true;
                            }
                        }

                        break;
                    // 结束标记
                    case XmlPullParser.END_TAG:
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
        if (flag) {
            OperatorLogDao operatorLogDao = LogisticsApplication.getInstance().getOperatorLogDao();
            operatorLogDao.deleteInTx(evaOperatorLogList);
        }
    }

    public void uploadOrderData() {
        uploadRecordData();
        uploadEvaRecordData();
    }

    private void uploadEvaRecordData() {
        StringBuilder sb = assemberEvaOrderDetail();
        Map params = new HashMap();
        params.put("value", sb.toString());
        String method = "Upload_kehupingjia";
        String action = "http://tempuri.org/Upload_kehupingjia";
        WebServiceParam webServiceParam =
                new WebServiceParam((Context) uploadCargoView, params, method, action, evaHandler, 1);
        webServiceConnect.addNet(webServiceParam);
    }

    private void uploadRecordData() {
        StringBuilder sb = assemberOrderDetail();
        Map params = new HashMap();
        params.put("value", sb.toString());
        String method = "UploadScanRecord";
        String action = "http://tempuri.org/UploadScanRecord";
        WebServiceParam webServiceParam =
                new WebServiceParam((Context) uploadCargoView, params, method, action, handler, 1);
        webServiceConnect.addNet(webServiceParam);
    }

    private StringBuilder assemberOrderDetail() {
        OperatorLogDao operatorLogDao = LogisticsApplication.getInstance().getOperatorLogDao();
        String userName = CommonTool.getSharePreference((Context) uploadCargoView, "userName");
        recordOperatorLogList = operatorLogDao.queryBuilder().where(OperatorLogDao.Properties.UserName.eq(userName), OperatorLogDao.Properties.OperType.eq("1")).list();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < recordOperatorLogList.size(); i++) {
            OperatorLog operatorLog = recordOperatorLogList.get(i);
            sb.append("DispatchNumber=").append(operatorLog.getDispatchNumber()).append(";")
                    .append("DateHappen=").append(operatorLog.getEditTime()).append(";")
                    .append("OrderID=").append(operatorLog.getOrderId()).append(";")
                    .append("Username=").append(operatorLog.getUserName()).append(";")
                    .append("MyType=").append(operatorLog.getMyType()).append(";")
                    .append("Longitude=0;Latitude=0;")
                    .append("L_PDTG_BATCH=").append(operatorLog.getLPdtgBatch()).append(";")
                    .append("DetailID=").append(operatorLog.getDetailId()).append("|");
        }
        return sb;
    }

    private StringBuilder assemberEvaOrderDetail() {
        OperatorLogDao operatorLogDao = LogisticsApplication.getInstance().getOperatorLogDao();
        String userName = CommonTool.getSharePreference((Context) uploadCargoView, "userName");
        evaOperatorLogList = operatorLogDao.queryBuilder().where(OperatorLogDao.Properties.UserName.eq(userName), OperatorLogDao.Properties.OperType.eq("2")).list();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < evaOperatorLogList.size(); i++) {
            OperatorLog operatorLog = evaOperatorLogList.get(i);
            sb.append("DispatchNumber=").append(operatorLog.getDispatchNumber()).append(";")
                    .append("DateHappen=").append(operatorLog.getEditTime()).append(";")
                    .append("OrderID=").append(operatorLog.getOrderId()).append(";")
                    .append("Username=").append(operatorLog.getUserName()).append(";")
                    .append("pingjianeirong=").append(operatorLog.getPingjianeirong()).append(";")
                    .append("DetailID=").append(operatorLog.getDetailId()).append("|");
        }
        return sb;
    }
}
