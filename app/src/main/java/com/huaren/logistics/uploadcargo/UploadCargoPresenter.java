package com.huaren.logistics.uploadcargo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;

import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.bean.ErrOperatorLog;
import com.huaren.logistics.bean.OperatorLog;
import com.huaren.logistics.bean.RecycleInput;
import com.huaren.logistics.bean.RecycleScan;
import com.huaren.logistics.dao.ErrOperatorLogDao;
import com.huaren.logistics.dao.OperatorLogDao;
import com.huaren.logistics.dao.RecycleInputDao;
import com.huaren.logistics.dao.RecycleScanDao;
import com.huaren.logistics.entity.UploadRecycleInput;
import com.huaren.logistics.entity.xml.Root;
import com.huaren.logistics.util.CommonTool;
import com.huaren.logistics.util.DateUtil;
import com.huaren.logistics.util.http.NetConnect;
import com.huaren.logistics.util.webservice.WebServiceConnect;
import com.huaren.logistics.util.webservice.WebServiceHandler;
import com.huaren.logistics.util.webservice.WebServiceParam;
import com.tencent.bugly.crashreport.BuglyLog;
import com.thoughtworks.xstream.XStream;

import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.QueryBuilder;

public class UploadCargoPresenter {

    private IUploadCargoView uploadCargoView;

    private List<OperatorLog> recordOperatorLogList;
    private List<OperatorLog> evaOperatorLogList;
    private List<RecycleInput> recycleInputList;
    private List<RecycleScan> recycleScanList;
    private List<ErrOperatorLog> errOperatorLogList;

    public WebServiceHandler recordHandler;
    public WebServiceHandler evaHandler;
    public WebServiceHandler inputHandler;
    public WebServiceHandler scanHandler;
    public WebServiceHandler errorHandler;
    protected WebServiceConnect webServiceConnect = new WebServiceConnect();
    private StringBuffer buffer = new StringBuffer("");
    private boolean isFinishOne = false;
    private boolean isFinishTwo = false;
    private boolean isFinishThree = false;
    private boolean isFinishFour = false;
    private boolean isFinishFive = false;

    public UploadCargoPresenter(final IUploadCargoView uploadCargoView) {
        this.uploadCargoView = uploadCargoView;

        recordHandler = new WebServiceHandler((Context) uploadCargoView) {
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

        errorHandler = new WebServiceHandler((Context) uploadCargoView) {
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
                        parseErrorRecordInfo(detail);
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
                    break;
                case 3:
                    isFinishThree = true;
                    break;
                case 4:
                    isFinishFour = true;
                    break;
                case 5:
                    isFinishFive = true;
                    break;
            }
            if (isFinishOne && isFinishTwo && isFinishThree && isFinishFour && isFinishFive) {
                String time = DateUtil.parseCurrDateToString("yyyy-MM-dd HH:mm:ss");
                uploadCargoView.showUpdateView(time, buffer.toString());
            }
        }
    };

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
            finishHandler.sendEmptyMessage(4);
        }
    }

    private void parseErrorRecordInfo(Object detail) {
        //批量信息
        SoapObject soapObject = (SoapObject) detail;
        String xml = soapObject.getPropertyAsString("Upload_yunshuyichangResult");
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
            buffer.append(",上传" + errOperatorLogList.size() + "条异常记录");
            ErrOperatorLogDao dao = LogisticsApplication.getInstance().getErrOperatorLogDao();
            dao.deleteInTx(errOperatorLogList);
            finishHandler.sendEmptyMessage(5);
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
        BuglyLog.d("ScanRecordInfo ", recordOperatorLogList.size() + " " + recordOperatorLogList.toString());
        BuglyLog.d("ScanRecordResult ", xml);
        boolean flag = false;
        XStream xStream = new XStream();
        xStream.alias("Root", Root.class);
        xStream.autodetectAnnotations(true);
        Root root = (Root) xStream.fromXML(xml);
        String msg = "";
        if (root != null && root.getLoginResult() != null) {
            if("1".equals(root.getLoginResult().getCode())) {
                flag = true;
            } else {
                flag = false;
            }
            if (root.getCommandResult() != null && root.getCommandResult().getMsg() != null) {
                msg = root.getCommandResult().getMsg();
            }
        }
        if (flag) {
            OperatorLogDao operatorLogDao = LogisticsApplication.getInstance().getOperatorLogDao();
            operatorLogDao.deleteInTx(recordOperatorLogList);
        }
        buffer.append("，上传" + recordOperatorLogList.size() + "条明细,服务器返回信息为" + msg);
        finishHandler.sendEmptyMessage(1);
        /*try {
            String msg = "";
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
            buffer.append("，上传" + recordOperatorLogList.size() + "条明细,服务器返回信息为");
            OperatorLogDao operatorLogDao = LogisticsApplication.getInstance().getOperatorLogDao();
            operatorLogDao.deleteInTx(recordOperatorLogList);
            finishHandler.sendEmptyMessage(1);
        }*/
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
            for (int i = 0; i < recycleInputList.size(); i++) {
                RecycleInput recycleInput = recycleInputList.get(i);
                recycleInput.setUpStatus("1");
            }
            dao.updateInTx(recycleInputList);
//            dao.deleteInTx(recycleInputList);
            finishHandler.sendEmptyMessage(3);
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
            finishHandler.sendEmptyMessage(2);
        }
    }

    public void uploadOrderData() {
        buffer = new StringBuffer("");
        uploadRecordData();
        uploadEvaRecordData();
        uploadRecycleInputData();
        uploadRecycleScanData();
        uploadErrorRecordData();
    }

    /**
     * 上传异常扫描信息
     */
    private void uploadErrorRecordData() {
        StringBuilder sb = assemberErrorRecordData();
        Map params = new HashMap();
        params.put("value", sb.toString());
        String method = "Upload_yunshuyichang";
        String action = "http://tempuri.org/Upload_yunshuyichang";
        WebServiceParam webServiceParam =
                new WebServiceParam((Context) uploadCargoView, params, method, action, errorHandler, 1);
        webServiceConnect.addNet(webServiceParam);
    }

    private StringBuilder assemberErrorRecordData() {
        ErrOperatorLogDao dao = LogisticsApplication.getInstance().getErrOperatorLogDao();
        String userName = CommonTool.getSharePreference((Context) uploadCargoView, "curUserName");
        errOperatorLogList = dao.queryBuilder().where(ErrOperatorLogDao.Properties.UserName.eq(userName)).list();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < errOperatorLogList.size(); i++) {
            ErrOperatorLog errOperatorLog = errOperatorLogList.get(i);
            sb.append("DateHappen=").append(DateUtil.parseDateToString(errOperatorLog.getAddTime(), DateUtil.DATE_TIME_FORMATE)).append(";")
                    .append("Username=").append(errOperatorLog.getCustomerId()).append(";")
                    .append("LPN=").append(errOperatorLog.getLpn()).append(";")
                    .append("DriversID=").append(errOperatorLog.getDriverId()).append(";")
                    .append("CooperateID=").append(errOperatorLog.getCooperateID()).append(";")
                    .append("L_PDTG_BATCH=").append(errOperatorLog.getLPdtgBatch()).append("|");
        }
        CommonTool.showLog("异常扫描====" + sb.toString());
        return sb;
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
        QueryBuilder<RecycleInput> qb = dao.queryBuilder();
        Date setDate = DateUtil.getDateBeforeOrAfter(new Date(), -30);
        DeleteQuery<RecycleInput> bd = qb.where(RecycleInputDao.Properties.RecycleTime.le(setDate), RecycleInputDao.Properties.UserName.eq(userName)).buildDelete();
        bd.executeDeleteWithoutDetachingEntities();
        recycleInputList = dao.queryBuilder().where(RecycleInputDao.Properties.UserName.eq(userName), RecycleInputDao.Properties.UpStatus.eq("0")).list();
        Map<String, Object> recycleInputMap = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < recycleInputList.size(); i++) {
            RecycleInput input = recycleInputList.get(i);
            String id = "" + input.getLPdtgBatch() + input.getCooperateId() + input.getDriversID();
            if (recycleInputMap.get(id) != null) {
                UploadRecycleInput uploadRecycleInput = (UploadRecycleInput) recycleInputMap.get(id);
                if (input.getRecycleTypeValue().equals("退货")) { //退货
                    uploadRecycleInput.setPiece1(input.getRecycleNum());
                } else if (input.getRecycleTypeValue().equals("调剂")) { //调剂
                    uploadRecycleInput.setPiece2(input.getRecycleNum());
                } else if (input.getRecycleTypeValue().equals("周转箱")) { //周转
                    uploadRecycleInput.setPiece(input.getRecycleNum());
                } else if(input.getRecycleTypeValue().equals("冷藏箱")){
                    uploadRecycleInput.setPiece3(input.getRecycleNum());
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
                if (input.getRecycleTypeValue().equals("退货")) { //退货
                    uploadRecycleInput.setPiece1(input.getRecycleNum());
                } else if (input.getRecycleTypeValue().equals("调剂")) { //调剂
                    uploadRecycleInput.setPiece2(input.getRecycleNum());
                } else if (input.getRecycleTypeValue().equals("周转箱")) { //周转
                    uploadRecycleInput.setPiece(input.getRecycleNum());
                } else if(input.getRecycleTypeValue().equals("冷藏箱")){
                    uploadRecycleInput.setPiece3(input.getRecycleNum());
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
                    .append("Piece2=").append(uploadRecycleInput.getPiece2()).append(";")
                    .append("Piece3=").append(uploadRecycleInput.getPiece3()).append("|");
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
                new WebServiceParam((Context) uploadCargoView, params, method, action, recordHandler, 1);
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
