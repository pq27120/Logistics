package com.huaren.logistics.cargo;

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

public class CargoPresenter {

  private ICargoView cargoView;

  public CargoPresenter(ICargoView cargoView) {
    this.cargoView = cargoView;
  }

  public void initCargoList() {
    CustomerDao customerDao = LogisticsApplication.getInstance().getDaoSession().getCustomerDao();
    List<Customer> customerList = customerDao.loadAll();
    for (int i = 0; i < customerList.size(); i++) {
      Customer customer = customerList.get(i);
      Map<String, Integer> countMap = countLoadOrder(customer);
      String desc = "订单总数："
          + countMap.get("total")
          + "，未装车订单："
          + countMap.get("unLoadCount")
          + "，已装车订单："
          + countMap.get("loadCount");
      int drawable = R.drawable.star_do;
      if (countMap.get("unLoadCount") == 0) {
        drawable = R.drawable.star_finish;
      }
      Card card = new Card.Builder((Context) cargoView).setTag(customer.getCustomerId())
          .withProvider(SmallImageCardProvider.class)
          .setTitle(customer.getName() + "(" + customer.getCustomerId() + ")")
          .setDescription(desc)
          .setDrawable(drawable)
          .endConfig()
          .build();
      cargoView.addCard(card);
    }
  }

  /**
   * 计算总货物、装车主订单数量、未装车主订单数量
   */
  private Map<String, Integer> countLoadOrder(Customer customer) {
    LogisticsOrderDao logisticsOrderDao = LogisticsApplication.getInstance().getLogisticsOrderDao();
    int total = 0, loadCount = 0, unLoadCount = 0;
    List<LogisticsOrder> logisticsOrderList = logisticsOrderDao.queryBuilder()
        .where(LogisticsOrderDao.Properties.CustomerId.eq(customer.getCustomerId()))
        .list();
    if (logisticsOrderList != null && !logisticsOrderList.isEmpty()) {
      total = logisticsOrderList.size();
      for (int i = 0; i < logisticsOrderList.size(); i++) {
        LogisticsOrder logisticsOrder = logisticsOrderList.get(i);
        if ("1".equals(logisticsOrder.getOrderStatus())) {
          unLoadCount++;
        } else if ("2".equals(logisticsOrder.getOrderStatus())) {
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
