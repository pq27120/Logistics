package com.huaren.logistics.evaluation;

import android.content.Context;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.SmallImageCardProvider;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.R;
import com.huaren.logistics.bean.Customer;
import com.huaren.logistics.bean.LogisticsOrder;
import com.huaren.logistics.dao.CustomerDao;
import com.huaren.logistics.dao.LogisticsOrderDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvaluationPresenter {
  private IEvaluationView evaluationView;

  public EvaluationPresenter(IEvaluationView evaluationView) {
    this.evaluationView = evaluationView;
  }

  public void initCargoList() {
    CustomerDao customerDao = LogisticsApplication.getInstance().getDaoSession().getCustomerDao();
    List<Customer> customerList = customerDao.loadAll();
    for (int i = 0; i < customerList.size(); i++) {
      Customer customer = customerList.get(i);
      Map<String, Integer> countMap = countLoadOrder(customer);
      String desc = "订单总数："
          + countMap.get("total")
          + "，未评价订单："
          + countMap.get("unLoadCount")
          + "，已评价订单："
          + countMap.get("loadCount");
      int drawable = R.drawable.star_do;
      if (countMap.get("unLoadCount") == 0) {
        drawable = R.drawable.star_finish;
      }
      Card card = new Card.Builder((Context) evaluationView).setTag(customer.getCooperateId())
          .withProvider(SmallImageCardProvider.class)
          .setTitle(customer.getCooperateName() + "(" + customer.getCooperateId() + ")")
          .setDescription(desc)
          .setDrawable(drawable)
          .endConfig()
          .build();
      evaluationView.addCard(card);
    }
  }

  /**
   * 计算总货物、装车主订单数量、未装车主订单数量
   */
  private Map<String, Integer> countLoadOrder(Customer customer) {
    LogisticsOrderDao logisticsOrderDao = LogisticsApplication.getInstance().getLogisticsOrderDao();
    int total = 0, loadCount = 0, unLoadCount = 0;
    List<LogisticsOrder> logisticsOrderList = logisticsOrderDao.queryBuilder()
        .where(LogisticsOrderDao.Properties.CooperateID.eq(customer.getCooperateId()))
        .list();
    if (logisticsOrderList != null && !logisticsOrderList.isEmpty()) {
      total = logisticsOrderList.size();
      for (int i = 0; i < logisticsOrderList.size(); i++) {
        LogisticsOrder logisticsOrder = logisticsOrderList.get(i);
        if ("4".equals(logisticsOrder.getOrderStatus())) {
          unLoadCount++;
        } else if ("5".equals(logisticsOrder.getOrderStatus())) {
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
