package com.huaren.logistics.evaluation;

import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.bean.SysDicValue;
import com.huaren.logistics.dao.SysDicValueDao;
import de.greenrobot.dao.query.QueryBuilder;
import java.util.List;

public class EvaluationDetailPresenter {
  private IEvaluationDetailView evaluationDetailView;

  public EvaluationDetailPresenter(IEvaluationDetailView evaluationDetailView) {
    this.evaluationDetailView = evaluationDetailView;
  }

  public void initEvaluationDetail(String orderId) {
    //LogisticsOrderDao logisticsOrderDao = LogisticsApplication.getInstance().getLogisticsOrderDao();
    //LogisticsOrder logisticsOrder = logisticsOrderDao.queryBuilder().where(LogisticsOrderDao.Properties.Ordered.eq(orderId)).unique();
    //if (logisticsOrder.getOrderStatus().equals(OrderStatusEnum.EVALUATION.getStatus())) {
    //  initEvaluationRadio(false, logisticsOrder.getEvaluation());
    //} else {
    //  initEvaluationRadio(true, null);
    //}
  }

  private void initEvaluationRadio(boolean flag, String evaluation) {
    SysDicValueDao sysDicValueDao = LogisticsApplication.getInstance().getSysDicValueDao();
    QueryBuilder qb = sysDicValueDao.queryBuilder();
    List<SysDicValue> list = qb.where(SysDicValueDao.Properties.DicId.eq(20)).list();
    evaluationDetailView.initRadio(list,flag,evaluation);
  }

  public boolean checkCustomerPass(String customerId, String pass) {
    return true;
  }

  public void evaluationOrder(String orderId, SysDicValue sysDicValue) {
    //LogisticsOrderDao logisticsOrderDao = LogisticsApplication.getInstance().getLogisticsOrderDao();
    //LogisticsOrder logisticsOrder = logisticsOrderDao.queryBuilder().where(LogisticsOrderDao.Properties.Ordered.eq(orderId)).unique();
    //logisticsOrder.setEvaluation("" + sysDicValue.getId());
    //logisticsOrder.setEditTime(new Date());
    //logisticsOrder.setOrderStatus(OrderStatusEnum.EVALUATION.getStatus());
    //logisticsOrderDao.update(logisticsOrder);
    //CommonTool.showLog(logisticsOrder.getEvaluation());
    //LogisticsApplication.getInstance().getSoundPoolUtil().playRight();
    //UiTool.showToast((Context)evaluationDetailView, "评价完成！");
    evaluationDetailView.back();
  }
}
