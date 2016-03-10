package com.huaren.logistics.evaluation;

import android.content.Context;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.SmallImageCardProvider;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.R;
import com.huaren.logistics.bean.LogisticsOrder;
import com.huaren.logistics.common.OrderStatusEnum;
import com.huaren.logistics.dao.LogisticsOrderDao;
import com.huaren.logistics.util.CommonTool;
import java.util.List;

public class EvaluationOrderPresenter {

  private IEvaluationOrderView cargoOrderView;

  public EvaluationOrderPresenter(IEvaluationOrderView cargoOrderView) {
    this.cargoOrderView = cargoOrderView;
  }

  public void initCargoOrder(String customerId) {
    LogisticsOrderDao logisticsOrderDao = LogisticsApplication.getInstance().getLogisticsOrderDao();
    List<LogisticsOrder> logisticsOrderList = logisticsOrderDao.queryBuilder()
        .whereOr(LogisticsOrderDao.Properties.OrderStatus.eq(OrderStatusEnum.EVALUATION.getStatus()),
            LogisticsOrderDao.Properties.OrderStatus.eq(OrderStatusEnum.UNCARGO.getStatus()))
        .where(LogisticsOrderDao.Properties.CooperateID.eq(customerId))
        .list();
    for (int i = 0; i < logisticsOrderList.size(); i++) {
      LogisticsOrder logisticsOrder = logisticsOrderList.get(i);
      String desc = "";
      int drawable = R.drawable.star_do;
      if (OrderStatusEnum.EVALUATION.getStatus().equals(logisticsOrder.getOrderStatus())) {
        desc = "已评价";
        drawable = R.drawable.star_finish;
      } else if (OrderStatusEnum.UNCARGO.getStatus().equals(logisticsOrder.getOrderStatus())) {
        desc = "未评价";
        drawable = R.drawable.star_do;
      }
      Card card = new Card.Builder((Context) cargoOrderView).setTag(logisticsOrder.getOrdered())
          .withProvider(SmallImageCardProvider.class)
          .setTitle(logisticsOrder.getOrdered())
          .setDescription(desc)
          .setDrawable(drawable)
          .endConfig()
          .build();
      cargoOrderView.addCard(card);
    }
  }
}
