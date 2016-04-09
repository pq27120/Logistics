package com.huaren.logistics.evaluation;

import android.content.Context;
import android.text.TextUtils;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.bean.Customer;
import com.huaren.logistics.bean.OrderBatch;
import com.huaren.logistics.bean.OrderDetail;
import com.huaren.logistics.bean.SysDicValue;
import com.huaren.logistics.common.OrderStatusEnum;
import com.huaren.logistics.dao.CustomerDao;
import com.huaren.logistics.dao.OrderBatchDao;
import com.huaren.logistics.dao.OrderDetailDao;
import com.huaren.logistics.dao.SysDicValueDao;
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
    OrderBatch orderBatch =
        orderBatchDao.queryBuilder().where(OrderBatchDao.Properties.Id.eq(orderBatchId)
        ,OrderBatchDao.Properties.Status.eq("1")).unique();
    if (!TextUtils.isEmpty(orderBatch.getEvaluation())) {
      initEvaluationRadio(false, orderBatch.getEvaluation());
    } else {
      initEvaluationRadio(true, null);
    }
  }

  private void initEvaluationRadio(boolean flag, String evaluation) {
    SysDicValueDao sysDicValueDao = LogisticsApplication.getInstance().getSysDicValueDao();
    QueryBuilder qb = sysDicValueDao.queryBuilder();
    List<SysDicValue> list = qb.where(SysDicValueDao.Properties.DicId.eq(20)).list();
    evaluationDetailView.initRadio(list, flag, evaluation);
  }

  public boolean checkCustomerPass(String orderBatchId, String pass) {
    OrderBatchDao orderBatchDao = LogisticsApplication.getInstance().getOrderBatchDao();
    OrderBatch orderBatch = orderBatchDao.queryBuilder()
        .where(OrderBatchDao.Properties.Id.eq(orderBatchId),
            OrderBatchDao.Properties.Status.eq("1"))
        .unique();
    CustomerDao customerDao = LogisticsApplication.getInstance().getCustomerDao();
    Customer customer = customerDao.queryBuilder()
        .where(CustomerDao.Properties.CooperateId.eq(orderBatch.getCooperateId()),
            CustomerDao.Properties.Status.eq("1"))
        .unique();
    if (customer.getCoopPwd().equals(pass)) {
      return true;
    }
    return false;
  }

  public void evaluationOrder(String orderBatchId, SysDicValue sysDicValue) {
    OrderBatchDao orderBatchDao = LogisticsApplication.getInstance().getOrderBatchDao();
    OrderBatch orderBatch = orderBatchDao.queryBuilder()
        .where(OrderBatchDao.Properties.Id.eq(orderBatchId),
            OrderBatchDao.Properties.Status.eq("1"))
        .unique();
    orderBatch.setEditTime(new Date());
    orderBatch.setEvaluation("" + sysDicValue.getId());
    orderBatchDao.update(orderBatch);

    OrderDetailDao orderDetailDao = LogisticsApplication.getInstance().getOrderDetailDao();
    List<OrderDetail> orderDetailList = orderDetailDao.queryBuilder()
        .where(OrderDetailDao.Properties.CooperateId.eq(orderBatch.getCooperateId()))
        .where(OrderDetailDao.Properties.DriversID.eq(orderBatch.getDriversID()))
        .where(OrderDetailDao.Properties.LPdtgBatch.eq(orderBatch.getLPdtgBatch()))
        .where(OrderDetailDao.Properties.DetailStatus.eq(OrderStatusEnum.UNCARGO.getStatus()))
        .list();
    for (int i = 0; i < orderDetailList.size(); i++) {
      OrderDetail evaluationDetail = orderDetailList.get(i);
      evaluationDetail.setEditTime(new Date());
      evaluationDetail.setDetailStatus(OrderStatusEnum.EVALUATION.getStatus());
      orderDetailDao.update(evaluationDetail);
    }
    LogisticsApplication.getInstance().getSoundPoolUtil().playRight();
    UiTool.showToast((Context) evaluationDetailView, "评价完成！");
    EvaluationDetailActivity.actionStart((Context) evaluationDetailView, orderBatchId);
    evaluationDetailView.finishActivity();
  }
}
