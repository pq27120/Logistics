package com.huaren.logistics.evaluation;

import android.content.Context;
import android.text.TextUtils;

import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.bean.Customer;
import com.huaren.logistics.bean.OperatorLog;
import com.huaren.logistics.bean.OrderBatch;
import com.huaren.logistics.bean.OrderDetail;
import com.huaren.logistics.bean.SysDicValue;
import com.huaren.logistics.common.OrderStatusEnum;
import com.huaren.logistics.dao.CustomerDao;
import com.huaren.logistics.dao.OperatorLogDao;
import com.huaren.logistics.dao.OrderBatchDao;
import com.huaren.logistics.dao.OrderDetailDao;
import com.huaren.logistics.dao.SysDicValueDao;
import com.huaren.logistics.recycleinput.RecycleInputDetailActivity;
import com.huaren.logistics.util.CommonTool;
import com.huaren.logistics.util.UiTool;

import de.greenrobot.dao.query.QueryBuilder;

import java.util.Date;
import java.util.List;

public class EvaluationDetailPresenter {
    private IEvaluationDetailView evaluationDetailView;

    public EvaluationDetailPresenter(IEvaluationDetailView evaluationDetailView) {
        this.evaluationDetailView = evaluationDetailView;
    }

    public void initEvaluationDetail(String orderBatchId) {
        OrderBatchDao orderBatchDao = LogisticsApplication.getInstance().getOrderBatchDao();
        String userName = CommonTool.getSharePreference((Context) evaluationDetailView, "curUserName");
        OrderBatch orderBatch =
                orderBatchDao.queryBuilder().where(OrderBatchDao.Properties.Id.eq(orderBatchId)
                        , OrderBatchDao.Properties.Status.eq("1"), OrderBatchDao.Properties.UserName.eq(userName)).unique();
        if (!TextUtils.isEmpty(orderBatch.getEvaluation())) {
            initEvaluationRadio(false, orderBatch.getEvaluation());
        } else {
            initEvaluationRadio(true, null);
        }
    }

    private void initEvaluationRadio(boolean flag, String evaluation) {
        SysDicValueDao sysDicValueDao = LogisticsApplication.getInstance().getSysDicValueDao();
        QueryBuilder qb = sysDicValueDao.queryBuilder();
        List<SysDicValue> list = qb.where(SysDicValueDao.Properties.DicId.eq(21)).list();
        evaluationDetailView.initRadio(list, flag, evaluation);
    }

    public boolean checkCustomerPass(String orderBatchId, String pass) {
        OrderBatchDao orderBatchDao = LogisticsApplication.getInstance().getOrderBatchDao();
        String userName = CommonTool.getSharePreference((Context) evaluationDetailView, "curUserName");
        OrderBatch orderBatch = orderBatchDao.queryBuilder()
                .where(OrderBatchDao.Properties.Id.eq(orderBatchId),
                        OrderBatchDao.Properties.Status.eq("1"), OrderBatchDao.Properties.UserName.eq(userName))
                .unique();
        CustomerDao customerDao = LogisticsApplication.getInstance().getCustomerDao();
        Customer customer = customerDao.queryBuilder()
                .where(CustomerDao.Properties.CooperateId.eq(orderBatch.getCooperateId()),
                        CustomerDao.Properties.Status.eq("1"), CustomerDao.Properties.UserName.eq(userName))
                .unique();
        if (customer.getCoopPwd().equals(pass)) {
            return true;
        }
        return false;
    }

    public void evaluationOrder(String orderBatchId, SysDicValue sysDicValue) {
        OrderBatchDao orderBatchDao = LogisticsApplication.getInstance().getOrderBatchDao();
        String userName = CommonTool.getSharePreference((Context) evaluationDetailView, "curUserName");
        OrderBatch orderBatch = orderBatchDao.queryBuilder()
                .where(OrderBatchDao.Properties.Id.eq(orderBatchId),
                        OrderBatchDao.Properties.Status.eq("1"), OrderBatchDao.Properties.UserName.eq(userName))
                .unique();
        orderBatch.setEditTime(new Date());
        orderBatch.setEvaluation("" + sysDicValue.getId());
        orderBatchDao.update(orderBatch);

        OrderDetailDao orderDetailDao = LogisticsApplication.getInstance().getOrderDetailDao();
        List<OrderDetail> orderDetailList = orderDetailDao.queryBuilder()
                .where(OrderDetailDao.Properties.CooperateId.eq(orderBatch.getCooperateId())
                        , OrderDetailDao.Properties.DriversID.eq(orderBatch.getDriversID())
                        , OrderDetailDao.Properties.LPdtgBatch.eq(orderBatch.getLPdtgBatch())
                        , OrderDetailDao.Properties.DetailStatus.eq(OrderStatusEnum.UNCARGO.getStatus())
                        , OrderDetailDao.Properties.UserName.eq(userName))
                .list();
        for (int i = 0; i < orderDetailList.size(); i++) {
            OrderDetail evaluationDetail = orderDetailList.get(i);
            evaluationDetail.setEditTime(new Date());
            evaluationDetail.setDetailStatus(OrderStatusEnum.EVALUATION.getStatus());
            orderDetailDao.update(evaluationDetail);

            OperatorLogDao operatorLogDao = LogisticsApplication.getInstance().getOperatorLogDao();
            OperatorLog operatorLog = new OperatorLog();
            operatorLog.setOperType("2");
            operatorLog.setUserName(userName);
            operatorLog.setDispatchNumber(evaluationDetail.getDispatchNumber());
            operatorLog.setEditTime(new Date());
            operatorLog.setLatitude(0);
            operatorLog.setLongitude(0);
            operatorLog.setLPdtgBatch(evaluationDetail.getLPdtgBatch());
            operatorLog.setMyType("2");
            operatorLog.setOrderId(evaluationDetail.getOrderId());
            operatorLog.setPingjianeirong(sysDicValue.getMyDisplayValue());
            operatorLog.setDetailId(evaluationDetail.getDetailId());
            operatorLogDao.insert(operatorLog);
        }
        LogisticsApplication.getInstance().getSoundPoolUtil().playRight();
        UiTool.showToast((Context) evaluationDetailView, "评价完成！");
        RecycleInputDetailActivity.actionStart((Context) evaluationDetailView, orderBatchId);
        evaluationDetailView.finishActivity();
    }
}
