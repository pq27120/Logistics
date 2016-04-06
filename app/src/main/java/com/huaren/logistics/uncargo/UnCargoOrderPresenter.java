package com.huaren.logistics.uncargo;

import android.content.Context;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.SmallImageCardProvider;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.R;
import com.huaren.logistics.bean.OrderDetail;
import com.huaren.logistics.common.OrderStatusEnum;
import com.huaren.logistics.dao.OrderDetailDao;
import com.huaren.logistics.util.CommonTool;
import com.huaren.logistics.util.StringTool;
import com.huaren.logistics.util.UiTool;
import de.greenrobot.dao.query.QueryBuilder;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class UnCargoOrderPresenter {

  private IUnCargoOrderView cargoOrderView;

  public UnCargoOrderPresenter(IUnCargoOrderView cargoOrderView) {
    this.cargoOrderView = cargoOrderView;
  }

  public void initCargoOrder(String customerId) {
    OrderDetailDao logisticsOrderDao = LogisticsApplication.getInstance().getOrderDetailDao();
    List<OrderDetail> logisticsOrderList = logisticsOrderDao.queryBuilder()
        .where(OrderDetailDao.Properties.CooperateId.eq(customerId),
            OrderDetailDao.Properties.DetailStatus.eq(OrderStatusEnum.CARGO.getStatus()))
        .list();
    for (int i = 0; i < logisticsOrderList.size(); i++) {
      OrderDetail orderDetail = logisticsOrderList.get(i);
      int drawable = R.drawable.star_do;
      String desc = CommonTool.getDescByStatus(orderDetail.getDetailStatus());
      Card card = new Card.Builder((Context) cargoOrderView).setTag(orderDetail.getDetailId())
          .withProvider(SmallImageCardProvider.class)
          .setTitle(orderDetail.getLpn() + "(" + orderDetail.getMtype() + ")")
          .setDescription(desc)
          .setDrawable(drawable)
          .endConfig()
          .build();
      cargoOrderView.addCard(card);
    }
  }

  public void loadDetailCargo(String customerId, String detailCode) {
    if (StringTool.isNotNull(detailCode)) {
      OrderDetailDao orderDetailDao = LogisticsApplication.getInstance().getOrderDetailDao();
      QueryBuilder qb = orderDetailDao.queryBuilder();
      qb.where(OrderDetailDao.Properties.Lpn.eq(detailCode));
      List<OrderDetail> orderDetailList = qb.list();
      if (orderDetailList != null && !orderDetailList.isEmpty()) {
        OrderDetail orderDetail = orderDetailList.get(0);
        if (OrderStatusEnum.UNCARGO.getStatus().equals(orderDetail.getDetailStatus())) {
          LogisticsApplication.getInstance().getSoundPoolUtil().playWrong();
          UiTool.showToast((Context) cargoOrderView, "货物已卸车！");
          return;
        } else if (!OrderStatusEnum.CARGO.getStatus().equals(orderDetail.getDetailStatus())) {
          LogisticsApplication.getInstance().getSoundPoolUtil().playWrong();
          UiTool.showToast((Context) cargoOrderView, "该货物无法卸车！");
          return;
        }
        if (orderDetail.getCooperateId().equals(customerId)) {
          updateOrderCargo(detailCode);
        } else {
          LogisticsApplication.getInstance().getSoundPoolUtil().playWrong();
          UiTool.showToast((Context) cargoOrderView, "不是当前客户" + customerId + "的货物，请不要卸车！");
        }
      } else {
        LogisticsApplication.getInstance().getSoundPoolUtil().playWrong();
        UiTool.showToast((Context) cargoOrderView, "货物信息不存在！");
      }
    } else {
      LogisticsApplication.getInstance().getSoundPoolUtil().playWrong();
      UiTool.showToast((Context) cargoOrderView, "请扫码后再卸车！");
    }
  }

  public void updateOrderCargo(String detailId) {
    OrderDetailDao orderDetailDao = LogisticsApplication.getInstance().getOrderDetailDao();
    QueryBuilder qb = orderDetailDao.queryBuilder();
    qb.where(OrderDetailDao.Properties.Lpn.eq(detailId));
    List<OrderDetail> orderDetailList = qb.list();
    if (orderDetailList != null && !orderDetailList.isEmpty()) {
      OrderDetail orderDetail = orderDetailList.get(0);
      orderDetail.setDetailStatus(OrderStatusEnum.UNCARGO.getStatus());
      orderDetail.setEditTime(new Date());
      orderDetailDao.insertOrReplaceInTx(orderDetail);
      LogisticsApplication.getInstance().getSoundPoolUtil().playRight();
      cargoOrderView.reInit();
    }
  }
}
