package com.huaren.logistics.evaluation;

import android.content.Context;
import android.text.TextUtils;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.CargoOrderCardProvider;
import com.dexafree.materialList.card.provider.SmallImageCardProvider;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.R;
import com.huaren.logistics.bean.OrderBatch;
import com.huaren.logistics.dao.OrderBatchDao;
import com.huaren.logistics.util.CommonTool;

import java.util.List;

public class EvaluationPresenter {
  private IEvaluationView evaluationView;

  public EvaluationPresenter(IEvaluationView evaluationView) {
    this.evaluationView = evaluationView;
  }

  public void initCargoList() {
    OrderBatchDao orderBatchDao = LogisticsApplication.getInstance().getOrderBatchDao();
    String userName = CommonTool.getSharePreference((Context) evaluationView, "curUserName");
    List<OrderBatch> orderBatchList =
        orderBatchDao.queryBuilder().where(OrderBatchDao.Properties.Status.eq("1"),
            OrderBatchDao.Properties.CanEvalutaion.eq("1"), OrderBatchDao.Properties.UserName.eq(userName)).list();
    for (int i = 0; i < orderBatchList.size(); i++) {
      OrderBatch orderBatch = orderBatchList.get(i);
      String desc = "未评价";
      int drawable = R.drawable.star_do;
      if (!TextUtils.isEmpty(orderBatch.getEvaluation())) {
        drawable = R.drawable.star_finish;
        desc = "已评价";
      }
      Card card = new Card.Builder((Context) evaluationView).setTag(orderBatch.getId())
          .withProvider(CargoOrderCardProvider.class)
          .setTitle(orderBatch.getSPdtgCustfullname() + "(" + orderBatch.getCooperateId() + ")")
          .setDescription(desc)
          .setDrawable(drawable)
          .endConfig()
          .build();
      evaluationView.addCard(card);
    }
  }
}
