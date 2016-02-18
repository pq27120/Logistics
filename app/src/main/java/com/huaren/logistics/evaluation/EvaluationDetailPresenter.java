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
    initEvaluationRadio();
  }

  private void initEvaluationRadio() {
    SysDicValueDao sysDicValueDao = LogisticsApplication.getInstance().getSysDicValueDao();
    QueryBuilder qb = sysDicValueDao.queryBuilder();
    List<SysDicValue> list = qb.where(SysDicValueDao.Properties.DicId.eq(1l)).list();
    evaluationDetailView.initRadio(list);
  }
}
