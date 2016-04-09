package com.huaren.logistics.uncargo;

import android.content.Context;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.SmallImageCardProvider;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.R;
import com.huaren.logistics.bean.Customer;
import com.huaren.logistics.bean.OrderDetail;
import com.huaren.logistics.common.OrderStatusEnum;
import com.huaren.logistics.dao.CustomerDao;
import com.huaren.logistics.dao.OrderDetailDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnCargoPresenter {

  private IUnCargoView unCargoView;

  public UnCargoPresenter(IUnCargoView unCargoView) {
    this.unCargoView = unCargoView;
  }

  public void initCargoList() {
    CustomerDao customerDao = LogisticsApplication.getInstance().getCustomerDao();
    List<Customer> customerList =
        customerDao.queryBuilder().where(CustomerDao.Properties.Status.eq("1")).list();
    for (int i = 0; i < customerList.size(); i++) {
      Customer customer = customerList.get(i);
      Map<String, Integer> countMap = countLoadOrder(customer);
      String desc = "总数："
          + countMap.get("total")
          + "，未卸车明细："
          + countMap.get("unLoadCount")
          + "，已卸车明细："
          + countMap.get("loadCount");
      int drawable = R.drawable.star_do;
      if (countMap.get("unLoadCount") == 0) {
        drawable = R.drawable.star_finish;
      }
      Card card = new Card.Builder((Context) unCargoView).setTag(customer.getId())
          .withProvider(SmallImageCardProvider.class)
          .setTitle(customer.getSPdtgCustfullname() + "(" + customer.getCooperateId() + ")")
          .setDescription(desc)
          .setDrawable(drawable)
          .endConfig()
          .build();
      unCargoView.addCard(card);
    }
  }

  /**
   * 计算总货物、卸车主订单数量、未卸车主订单数量
   */
  private Map<String, Integer> countLoadOrder(Customer customer) {
    OrderDetailDao orderDetailDao = LogisticsApplication.getInstance().getOrderDetailDao();
    int total = 0, loadCount = 0, unLoadCount = 0;
    List<OrderDetail> orderDetailList = orderDetailDao.queryBuilder()
        .where(OrderDetailDao.Properties.CustomerId.eq(customer.getId()),
            OrderDetailDao.Properties.Status.eq("1"))
        .list();
    if (orderDetailList != null && !orderDetailList.isEmpty()) {
      total = orderDetailList.size();
      for (int i = 0; i < orderDetailList.size(); i++) {
        OrderDetail orderDetail = orderDetailList.get(i);
        if (OrderStatusEnum.CARGO.getStatus().equals(orderDetail.getDetailStatus())) {
          unLoadCount++;
        } else if (OrderStatusEnum.UNCARGO.getStatus().equals(orderDetail.getDetailStatus())) {
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
