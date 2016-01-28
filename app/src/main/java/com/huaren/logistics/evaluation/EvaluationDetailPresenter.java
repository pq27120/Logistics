package com.huaren.logistics.evaluation;

import android.content.Context;
import com.huaren.logistics.util.CommonTool;

public class EvaluationDetailPresenter {
  private IEvaluationDetailView evaluationDetailView;

  public EvaluationDetailPresenter(IEvaluationDetailView evaluationDetailView) {
    this.evaluationDetailView = evaluationDetailView;
  }

  public void initUserInfo() {
    String name = CommonTool.getSharePreference((Context) evaluationDetailView, "name");
    String driver = CommonTool.getSharePreference((Context) evaluationDetailView, "name");
    String licensePlate = CommonTool.getSharePreference((Context) evaluationDetailView, "licensePlate");
    evaluationDetailView.setUserInfo(name, driver, licensePlate);
  }

  public void initEvaluationDetail() {

  }
}
