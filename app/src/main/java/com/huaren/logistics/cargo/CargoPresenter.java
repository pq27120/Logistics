package com.huaren.logistics.cargo;

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
import com.huaren.logistics.util.CommonTool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CargoPresenter {

  private ICargoView cargoView;

  public CargoPresenter(ICargoView cargoView) {
    this.cargoView = cargoView;
  }

  public void initCargoList() {
    CustomerDao customerDao = LogisticsApplication.getInstance().getCustomerDao();
    String userName = CommonTool.getSharePreference((Context) cargoView, "userName");
    List<Customer> customerList = customerDao.queryBuilder().where(CustomerDao.Properties.Status.eq("1"), CustomerDao.Properties.UserName.eq(userName)).list();
    for (int i = 0; i < customerList.size(); i++) {
      Customer customer = customerList.get(i);
      Map<String, Integer> countMap = countLoadOrder(customer);
      String desc = "总数："
          + countMap.get("total")
          + "，未装车明细："
          + countMap.get("unLoadCount")
          + "，已装车明细："
          + countMap.get("loadCount");
      int drawable = R.drawable.star_do;
      if (countMap.get("unLoadCount") == 0) {
        drawable = R.drawable.star_finish;
      }
      Card card = new Card.Builder((Context) cargoView).setTag(customer.getId())
          .withProvider(SmallImageCardProvider.class)
          .setTitle(customer.getSPdtgCustfullname() + "(" + customer.getCooperateId() + ")")
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
    OrderDetailDao orderDetailDao = LogisticsApplication.getInstance().getOrderDetailDao();
    int total = 0, loadCount = 0, unLoadCount = 0;
    String userName = CommonTool.getSharePreference((Context) cargoView, "userName");
    List<OrderDetail> orderDetailList = orderDetailDao.queryBuilder()
        .where(OrderDetailDao.Properties.CustomerId.eq(customer.getId())
            , OrderDetailDao.Properties.Status.eq("1"), OrderDetailDao.Properties.UserName.eq(userName))
        .list();
    if (orderDetailList != null && !orderDetailList.isEmpty()) {
      total = orderDetailList.size();
      for (int i = 0; i < orderDetailList.size(); i++) {
        OrderDetail orderDetail = orderDetailList.get(i);
        if (OrderStatusEnum.READEY_CARGO.getStatus().equals(orderDetail.getDetailStatus())) {
          unLoadCount++;
        } else if (OrderStatusEnum.CARGO.getStatus().equals(orderDetail.getDetailStatus())) {
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
