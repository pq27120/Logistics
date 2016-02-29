package com.huaren.logistics.evaluation;

import android.content.Context;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.SmallImageCardProvider;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.R;
import com.huaren.logistics.bean.LogisticsOrder;
import com.huaren.logistics.bean.OrderDetail;
import com.huaren.logistics.dao.LogisticsOrderDao;
import com.huaren.logistics.dao.OrderDetailDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvaluationOrderPresenter {

  private IEvaluationOrderView cargoOrderView;

  public EvaluationOrderPresenter(IEvaluationOrderView cargoOrderView) {
    this.cargoOrderView = cargoOrderView;
  }

  public void initCargoOrder(String customerId) {
    LogisticsOrderDao logisticsOrderDao = LogisticsApplication.getInstance().getLogisticsOrderDao();
    List<LogisticsOrder> logisticsOrderList = logisticsOrderDao.queryBuilder()
        .where(LogisticsOrderDao.Properties.CooperateID.eq(customerId))
        .list();
    for (int i = 0; i < logisticsOrderList.size(); i++) {
      LogisticsOrder logisticsOrder = logisticsOrderList.get(i);
      Map<String, Integer> countMap = countLoadOrder(logisticsOrder);
      String desc = "货物总数："
          + countMap.get("total")
          + "，未评价订单："
          + countMap.get("unLoadCount")
          + "，已评价订单："
          + countMap.get("loadCount");
      int drawable = R.drawable.star_do;
      if (countMap.get("unLoadCount") == 0) {
        drawable = R.drawable.star_finish;
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

  /**
   * 计算订单总数、装车货物数量、未装车货物数量
   */
  private Map<String, Integer> countLoadOrder(LogisticsOrder logisticsOrder) {
    OrderDetailDao orderDetailDao = LogisticsApplication.getInstance().getOrderDetailDao();
    int total = 0, loadCount = 0, unLoadCount = 0;
    List<OrderDetail> orderDetailList = orderDetailDao.queryBuilder()
        .where(OrderDetailDao.Properties.Ordered.eq(logisticsOrder.getOrdered()))
        .list();
    if (orderDetailList != null && !orderDetailList.isEmpty()) {
      total = orderDetailList.size();
      for (int i = 0; i < orderDetailList.size(); i++) {
        OrderDetail orderDetail = orderDetailList.get(i);
        if ("4".equals(orderDetail.getDetailStatus())) {
          unLoadCount++;
        } else if ("5".equals(orderDetail.getDetailStatus())) {
          loadCount++;
        }
      }
    }
    Map<String, Integer> map = new HashMap<>();
    map.put("total", total);
    map.put("unLoadCount", unLoadCount);
    map.put("loadCount", loadCount);
    return map;
  }
}
