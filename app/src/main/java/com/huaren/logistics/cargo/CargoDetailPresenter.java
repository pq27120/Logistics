package com.huaren.logistics.cargo;

import android.content.Context;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.SmallImageCardProvider;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.R;
import com.huaren.logistics.bean.OrderDetail;
import com.huaren.logistics.common.OrderStatusEnum;
import com.huaren.logistics.dao.OrderDetailDao;
import com.huaren.logistics.util.CommonTool;
import java.util.List;

public class CargoDetailPresenter {
  private ICargoDetailView cargoDetailView;

  public CargoDetailPresenter(ICargoDetailView cargoDetailView) {
    this.cargoDetailView = cargoDetailView;
  }

  public void initCargoDetail(String orderId) {
    OrderDetailDao orderDetailDao =
        LogisticsApplication.getInstance().getDaoSession().getOrderDetailDao();
    List<OrderDetail> orderDetailList = orderDetailDao.queryBuilder()
        .where(OrderDetailDao.Properties.Ordered.eq(orderId),
            OrderDetailDao.Properties.DetailStatus.eq(OrderStatusEnum.READEY_CARGO.getStatus()))
        .list();
    for (int i = 0; i < orderDetailList.size(); i++) {
      OrderDetail orderDetail = orderDetailList.get(i);
      int drawable = R.drawable.star_do;
      String desc = CommonTool.getDescByStatus(orderDetail.getDetailStatus());
      Card card = new Card.Builder((Context) cargoDetailView).setTag(orderDetail.getGoodsId())
          .withProvider(SmallImageCardProvider.class)
          .setTitle(orderDetail.getGoodsName() + "(" + orderDetail.getLpn() + ")")
          .setDescription(desc)
          .setDrawable(drawable)
          .endConfig()
          .build();
      cargoDetailView.addCard(card);
    }
  }
}
