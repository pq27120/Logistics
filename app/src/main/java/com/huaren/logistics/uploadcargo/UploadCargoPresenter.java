package com.huaren.logistics.uploadcargo;

import android.content.Context;
import android.util.Xml;

import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.bean.OperatorLog;
import com.huaren.logistics.bean.RecycleInput;
import com.huaren.logistics.bean.RecycleScan;
import com.huaren.logistics.dao.OperatorLogDao;
import com.huaren.logistics.dao.RecycleInputDao;
import com.huaren.logistics.dao.RecycleScanDao;
import com.huaren.logistics.entity.UploadRecycleInput;
import com.huaren.logistics.util.CommonTool;
import com.huaren.logistics.util.DateUtil;
import com.huaren.logistics.util.http.NetConnect;
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
    private List<RecycleInput> recycleInputList;
    private List<RecycleScan> recycleScanList;

    public WebServiceHandler handler;
    public WebServiceHandler evaHandler;
    public WebServiceHandler inputHandler;
    public WebServiceHandler scanHandler;
    protected WebServiceConnect webServiceConnect = new WebServiceConnect();
    private StringBuffer buffer = new StringBuffer("");

    public UploadCargoPresenter(final IUploadCargoView uploadCargoView) {
        this.uploadCargoView = uploadCargoView;

        handler = new WebServiceHandler((Context) uploadCargoView) {
            @Override
            public void handleFirst() {

            }

            @Override
            public void handleMsg(int returnCode, Object detail) {
                switch (returnCode) {
                    case NetConnect.NET_ERROR:
                        uploadCargoView.finishActivity();
                        break;
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
                    case NetConnect.NET_ERROR:
                        uploadCargoView.finishActivity();
                        break;
                    case 1:
                        parseEvaRecordInfo(detail);
                        break;
                }
            }
        };

        inputHandler = new WebServiceHandler((Context) uploadCargoView) {
            @Override
            public void handleFirst() {

            }

            @Override
            public void handleMsg(int returnCode, Object detail) {
                switch (returnCode) {
                    case NetConnect.NET_ERROR:
                        uploadCargoView.finishActivity();
                        break;
                    case 1:
                        parseRecycleInputInfo(detail);
                        break;
                }
            }
        };

        scanHandler = new WebServiceHandler((Context) uploadCargoView) {
            @Override
            public void handleFirst() {

            }

            @Override
            public void handleMsg(int returnCode, Object detail) {
                switch (returnCode) {
                    case NetConnect.NET_ERROR:
                        uploadCargoView.finishActivity();
                        break;
                    case 1:
                        parseRecycleScanInfo(detail);
                        break;
                }
            }
        };
    }

    private void parseRecycleInputInfo(Object detail) {
        parseRecycleInputXml(detail);
    }

    private void parseRecycleScanInfo(Object detail) {
        parseRecycleScanXml(detail);
    }

    private void parseRecycleScanXml(Object detail) {
        //批量信息
        SoapObject soapObject = (SoapObject) detail;
        String xml = soapObject.getPropertyAsString("Upload_kehuhuishouDetailResult");
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
                        if ("LoginResult".equals(name)) {
                            flag = false;
                        }
                        if ("Code".equals(name)) {
                            String code = parser.nextText();
                            if ("1".equals(code)) {
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
            buffer.append(",上传" + recycleScanList.size() + "条回收扫描记录");
            RecycleScanDao dao = LogisticsApplication.getInstance().getRecycleScanDao();
            dao.deleteInTx(recycleScanList);
            String time = DateUtil.parseCurrDateToString("yyyy-MM-dd HH:mm:ss");
            uploadCargoView.showUpdateView(time, buffer.toString());
        }
    }

    private void parseRecordInfo(Object detail) {
        parseRecordXml(detail);
    }

    private void parseEvaRecordInfo(Object detail) {
        parseEvaRecordXml(detail);
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
                        if ("LoginResult".equals(name)) {
                            flag = false;
                        }
                        if ("Code".equals(name)) {
                            String code = parser.nextText();
                            if ("1".equals(code)) {
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
            buffer.append("，上传" + recordOperatorLogList.size() + "条明细");
            OperatorLogDao operatorLogDao = LogisticsApplication.getInstance().getOperatorLogDao();
            operatorLogDao.deleteInTx(recordOperatorLogList);
        }
    }

    private void parseRecycleInputXml(Object detail) {
        //批量信息
        SoapObject soapObject = (SoapObject) detail;
        String xml = soapObject.getPropertyAsString("Upload_kehuhuishouResult");
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
                        if ("LoginResult".equals(name)) {
                            flag = false;
                        }
                        if ("Code".equals(name)) {
                            String code = parser.nextText();
                            if ("1".equals(code)) {
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
            buffer.append(",上传" + recycleInputList.size() + "条回收记录");
            RecycleInputDao dao = LogisticsApplication.getInstance().getRecycleInputDao();
            dao.deleteInTx(recycleInputList);
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
                        if ("LoginResult".equals(name)) {
                            flag = false;
                        }
                        if ("Code".equals(name)) {
                            String code = parser.nextText();
                            if ("1".equals(code)) {
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
            buffer.append(",上传" + evaOperatorLogList.size() + "条评价");
            OperatorLogDao operatorLogDao = LogisticsApplication.getInstance().getOperatorLogDao();
            operatorLogDao.deleteInTx(evaOperatorLogList);
        }
    }

    public void uploadOrderData() {
        uploadRecordData();
        uploadEvaRecordData();
        uploadRecycleInputData();
        uploadRecycleScanData();
    }

    private void uploadRecycleScanData() {
        StringBuilder sb = assemberRecycleScanDetail();
        Map params = new HashMap();
        params.put("value", sb.toString());
        String method = "Upload_kehuhuishouDetail";
        String action = "http://tempuri.org/Upload_kehuhuishouDetail";
        WebServiceParam webServiceParam =
                new WebServiceParam((Context) uploadCargoView, params, method, action, scanHandler, 1);
        webServiceConnect.addNet(webServiceParam);
    }

    private StringBuilder assemberRecycleScanDetail() {
        RecycleScanDao dao = LogisticsApplication.getInstance().getRecycleScanDao();
        String userName = CommonTool.getSharePreference((Context) uploadCargoView, "curUserName");
        recycleScanList = dao.queryBuilder().where(RecycleScanDao.Properties.UserName.eq(userName)).list();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < recycleScanList.size(); i++) {
            RecycleScan recycleScan = recycleScanList.get(i);
            sb.append("DateHappen=").append(DateUtil.parseDateToString(recycleScan.getRecycleScanTime(), DateUtil.DATE_TIME_FORMATE)).append(";")
                    .append("Username=").append(recycleScan.getUserName()).append(";")
                    .append("LPN=").append(recycleScan.getScanCode()).append(";")
                    .append("huishouleixing=").append(recycleScan.getRecycleTypeValue()).append("|");
        }
        CommonTool.showLog("回收扫描====" + sb.toString());
        return sb;
    }

    private void uploadRecycleInputData() {
        StringBuilder sb = assemberRecycleInputDetail();
        Map params = new HashMap();
        params.put("value", sb.toString());
        String method = "Upload_kehuhuishou ";
        String action = "http://tempuri.org/Upload_kehuhuishou";
        WebServiceParam webServiceParam =
                new WebServiceParam((Context) uploadCargoView, params, method, action, inputHandler, 1);
        webServiceConnect.addNet(webServiceParam);
    }

    private StringBuilder assemberRecycleInputDetail() {
        RecycleInputDao dao = LogisticsApplication.getInstance().getRecycleInputDao();
        String userName = CommonTool.getSharePreference((Context) uploadCargoView, "curUserName");
        recycleInputList = dao.queryBuilder().where(RecycleInputDao.Properties.UserName.eq(userName)).list();
        Map<String, Object> recycleInputMap = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < recycleInputList.size(); i++) {
            RecycleInput input = recycleInputList.get(i);
            String id = "" + input.getLPdtgBatch() + input.getCooperateId() + input.getDriversID();
            if (recycleInputMap.get(id) != null) {
                UploadRecycleInput uploadRecycleInput = (UploadRecycleInput) recycleInputMap.get(id);
                if (input.getRecycleType() == 77) { //退货
                    uploadRecycleInput.setPiece1(input.getRecycleNum());
                } else if (input.getRecycleType() == 79) { //调剂
                    uploadRecycleInput.setPiece2(input.getRecycleNum());
                } else if (input.getRecycleType() == 80) { //周转
                    uploadRecycleInput.setPiece(input.getRecycleNum());
                }
            } else {
                UploadRecycleInput uploadRecycleInput = new UploadRecycleInput();
                uploadRecycleInput.setCooperateId(input.getCooperateId());
                uploadRecycleInput.setDriversID(input.getDriversID());
                uploadRecycleInput.setEditTime(input.getEditTime());
                uploadRecycleInput.setOrderBatchId(input.getOrderBatchId());
                uploadRecycleInput.setlPdtgBatch(input.getLPdtgBatch());
                uploadRecycleInput.setRecycleTime(input.getRecycleTime());
                uploadRecycleInput.setUserName(input.getUserName());
                if (input.getRecycleType() == 77) { //退货
                    uploadRecycleInput.setPiece1(input.getRecycleNum());
                } else if (input.getRecycleType() == 79) { //调剂
                    uploadRecycleInput.setPiece2(input.getRecycleNum());
                } else if (input.getRecycleType() == 80) { //周转
                    uploadRecycleInput.setPiece(input.getRecycleNum());
                }
                recycleInputMap.put(id, uploadRecycleInput);
            }
        }
        for (Map.Entry entry : recycleInputMap.entrySet()) {
            UploadRecycleInput uploadRecycleInput = (UploadRecycleInput) entry.getValue();
            sb.append("DateHappen=").append(DateUtil.parseDateToString(uploadRecycleInput.getRecycleTime(), DateUtil.DATE_TIME_FORMATE)).append(";")
                    .append("Username=").append(uploadRecycleInput.getUserName()).append(";")
                    .append("DriversId=").append(uploadRecycleInput.getDriversID()).append(";")
//                    .append("S_PDTG_EMPLNAME=").append(uploadRecycleInput.getUserName()).append(";")
                    .append("CooperateID=").append(uploadRecycleInput.getCooperateId()).append(";")
//                    .append("S_PDTG_CUSTFULLNAME=").append(uploadRecycleInput.getCooperateId()).append(";")
                    .append("L_PDTG_BATCH=").append(uploadRecycleInput.getlPdtgBatch()).append(";")
                    .append("Piece=").append(uploadRecycleInput.getPiece()).append(";")
                    .append("Piece1=").append(uploadRecycleInput.getPiece1()).append(";")
                    .append("Piece2=").append(uploadRecycleInput.getPiece2()).append("|");
        }
        CommonTool.showLog("回收录入====" + sb.toString());
        return sb;
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
        String userName = CommonTool.getSharePreference((Context) uploadCargoView, "curUserName");
        recordOperatorLogList = operatorLogDao.queryBuilder().where(OperatorLogDao.Properties.UserName.eq(userName), OperatorLogDao.Properties.OperType.eq("1")).list();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < recordOperatorLogList.size(); i++) {
            OperatorLog operatorLog = recordOperatorLogList.get(i);
            sb.append("DispatchNumber=").append(operatorLog.getDispatchNumber()).append(";")
                    .append("DateHappen=").append(DateUtil.parseDateToString(operatorLog.getEditTime(), DateUtil.DATE_TIME_FORMATE)).append(";")
                    .append("OrderID=").append(operatorLog.getOrderId()).append(";")
                    .append("Username=").append(operatorLog.getUserName()).append(";")
                    .append("MyType=").append(operatorLog.getMyType()).append(";")
                    .append("Longitude=0;Latitude=0;")
                    .append("L_PDTG_BATCH=").append(operatorLog.getLPdtgBatch()).append(";")
                    .append("DetailID=").append(operatorLog.getDetailId()).append("|");
        }
        CommonTool.showLog("上传记录====" + sb.toString());
        return sb;
    }

    private StringBuilder assemberEvaOrderDetail() {
        OperatorLogDao operatorLogDao = LogisticsApplication.getInstance().getOperatorLogDao();
        String userName = CommonTool.getSharePreference((Context) uploadCargoView, "curUserName");
        evaOperatorLogList = operatorLogDao.queryBuilder().where(OperatorLogDao.Properties.UserName.eq(userName), OperatorLogDao.Properties.OperType.eq("2")).list();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < evaOperatorLogList.size(); i++) {
            OperatorLog operatorLog = evaOperatorLogList.get(i);
            sb.append("DispatchNumber=").append(operatorLog.getDispatchNumber()).append(";")
                    .append("DateHappen=").append(DateUtil.parseDateToString(operatorLog.getEditTime(), DateUtil.DATE_TIME_FORMATE)).append(";")
                    .append("OrderID=").append(operatorLog.getOrderId()).append(";")
                    .append("Username=").append(operatorLog.getUserName()).append(";")
                    .append("pingjianeirong=").append(operatorLog.getPingjianeirong()).append(";")
                    .append("DetailID=").append(operatorLog.getDetailId()).append("|");
        }
        CommonTool.showLog("评价内容====" + sb.toString());
        return sb;
    }
}
