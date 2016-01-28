package com.huaren.logistics.uncargo;

import android.content.Context;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.SmallImageCardProvider;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.R;
import com.huaren.logistics.bean.LogisticsOrder;
import com.huaren.logistics.bean.OrderDetail;
import com.huaren.logistics.dao.LogisticsOrderDao;
import com.huaren.logistics.dao.OrderDetailDao;
import com.huaren.logistics.util.StringTool;
import com.huaren.logistics.util.UiTool;
import de.greenrobot.dao.query.QueryBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnCargoOrderPresenter {

  private IUnCargoOrderView cargoOrderView;

  public UnCargoOrderPresenter(IUnCargoOrderView cargoOrderView) {
    this.cargoOrderView = cargoOrderView;
  }

  public void initCargoOrder(String customerId) {
    LogisticsOrderDao logisticsOrderDao = LogisticsApplication.getInstance().getLogisticsOrderDao();
    List<LogisticsOrder> logisticsOrderList = logisticsOrderDao.queryBuilder()
        .where(LogisticsOrderDao.Properties.CustomerId.eq(customerId))
        .list();
    for (int i = 0; i < logisticsOrderList.size(); i++) {
      LogisticsOrder logisticsOrder = logisticsOrderList.get(i);
      Map<String, Integer> countMap = countLoadOrder(logisticsOrder);
      String desc = "货物总数："
          + countMap.get("total")
          + "，未卸车货物："
          + countMap.get("unLoadCount")
          + "，已卸车货物："
          + countMap.get("loadCount");
      int drawable = R.drawable.star_do;
      if (countMap.get("unLoadCount") == 0) {
        drawable = R.drawable.star_finish;
      }
      Card card = new Card.Builder((Context) cargoOrderView).setTag(logisticsOrder.getOrderId())
          .withProvider(SmallImageCardProvider.class)
          .setTitle(logisticsOrder.getOrderName() + "(" + logisticsOrder.getOrderId() + ")")
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
        .where(OrderDetailDao.Properties.OrderId.eq(logisticsOrder.getOrderId()))
        .list();
    if (orderDetailList != null && !orderDetailList.isEmpty()) {
      total = orderDetailList.size();
      for (int i = 0; i < orderDetailList.size(); i++) {
        OrderDetail orderDetail = orderDetailList.get(i);
        if ("2".equals(orderDetail.getDetailStatus())) {
          unLoadCount++;
        } else if ("3".equals(orderDetail.getDetailStatus())) {
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

  public void loadDetailCargo(String customerId, String detailCode) {
    if (StringTool.isNotNull(detailCode)) {
      OrderDetailDao orderDetailDao = LogisticsApplication.getInstance().getOrderDetailDao();
      QueryBuilder qb = orderDetailDao.queryBuilder();
      qb.where(OrderDetailDao.Properties.DetailId.eq(detailCode));
      List<OrderDetail> orderDetailList = qb.list();
      if (orderDetailList != null && !orderDetailList.isEmpty()) {
        OrderDetail orderDetail = orderDetailList.get(0);
        if ("3".equals(orderDetail.getDetailStatus())) {
          UiTool.showToast((Context) cargoOrderView, "货物已卸车！");
          return;
        } else if (!"2".equals(orderDetail.getDetailStatus())) {
          UiTool.showToast((Context) cargoOrderView, "该货物无法卸车！");
          return;
        }
        LogisticsOrderDao logisticsOrderDao =
            LogisticsApplication.getInstance().getDaoSession().getLogisticsOrderDao();
        QueryBuilder logisQb = logisticsOrderDao.queryBuilder();
        logisQb.where(OrderDetailDao.Properties.OrderId.eq(orderDetail.getOrderId()));
        List<LogisticsOrder> logisticsOrderList = logisQb.list();
        if (logisticsOrderList != null && !logisticsOrderList.isEmpty()) {
          LogisticsOrder logisticsOrder = logisticsOrderList.get(0);
          if (logisticsOrder.getCustomerId().equals(customerId)) {
            cargoOrderView.showLoadDialog("卸车", "是否卸车？");
          } else {
            UiTool.showToast((Context) cargoOrderView, "不是当前客户" + customerId + "的货物，请不要卸车！");
          }
        } else {
          UiTool.showToast((Context) cargoOrderView, "货物信息不存在！");
        }
      } else {
        UiTool.showToast((Context) cargoOrderView, "货物信息不存在！");
      }
    } else {
      UiTool.showToast((Context) cargoOrderView, "请扫码后再装车！");
    }
  }

  public void updateOrderCargo(String detailId) {
    OrderDetailDao orderDetailDao =
        LogisticsApplication.getInstance().getDaoSession().getOrderDetailDao();
    QueryBuilder qb = orderDetailDao.queryBuilder();
    qb.where(OrderDetailDao.Properties.DetailId.eq(detailId));
    List<OrderDetail> orderDetailList = qb.list();
    if (orderDetailList != null && !orderDetailList.isEmpty()) {
      OrderDetail orderDetail = orderDetailList.get(0);
      orderDetail.setDetailStatus("2");
      orderDetailDao.insertOrReplaceInTx(orderDetail);
      updateOrderStatus(orderDetail.getOrderId());
    }
  }

  private void updateOrderStatus(String orderId) {
    LogisticsOrderDao logisticsOrderDao =
        LogisticsApplication.getInstance().getDaoSession().getLogisticsOrderDao();
    OrderDetailDao orderDetailDao =
        LogisticsApplication.getInstance().getDaoSession().getOrderDetailDao();
    QueryBuilder qb = orderDetailDao.queryBuilder();
    qb.where(OrderDetailDao.Properties.OrderId.eq(orderId));
    List<OrderDetail> orderDetailList = qb.list();
    boolean flag = true;
    for (int i = 0; i < orderDetailList.size(); i++) {
      OrderDetail orderDetail = orderDetailList.get(i);
      if ("1".equals(orderDetail.getDetailStatus())) {
        flag = false;
      }
    }
    if (flag) {
      QueryBuilder qb1 = logisticsOrderDao.queryBuilder();
      qb1.where(LogisticsOrderDao.Properties.OrderId.eq(orderId));
      List<LogisticsOrder> logisticsOrderList = qb1.list();
      if (logisticsOrderList != null && !logisticsOrderList.isEmpty()) {
        LogisticsOrder logisticsOrder = logisticsOrderList.get(0);
        if ("1".equals(logisticsOrder.getOrderStatus())) {
          logisticsOrder.setOrderStatus("2");
          logisticsOrderDao.insertOrReplaceInTx(logisticsOrder);
        }
      }
    }
    cargoOrderView.reInit();
  }
}
