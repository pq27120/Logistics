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
import java.util.List;

public class UnCargoDetailPresenter{

  private IUnCargoDetailView unCargoDetailView;

  public UnCargoDetailPresenter(IUnCargoDetailView unCargoDetailView) {
    this.unCargoDetailView = unCargoDetailView;
  }

  public void initCargoDetail(String orderId) {
    OrderDetailDao orderDetailDao =
        LogisticsApplication.getInstance().getDaoSession().getOrderDetailDao();
    List<OrderDetail> orderDetailList =
        orderDetailDao.queryBuilder().where(OrderDetailDao.Properties.Ordered.eq(orderId)).list();
    for (int i = 0; i < orderDetailList.size(); i++) {
      OrderDetail orderDetail = orderDetailList.get(i);
      int drawable;
      if (OrderStatusEnum.CARGO.getStatus().equals(orderDetail.getDetailStatus())) {
        drawable = R.drawable.star_do;
      } else {
        drawable = R.drawable.star_finish;
      }
      String desc = CommonTool.getDescByStatus(orderDetail.getDetailStatus());
      Card card = new Card.Builder((Context) unCargoDetailView).setTag(orderDetail.getGoodsId())
          .withProvider(SmallImageCardProvider.class)
          .setTitle(orderDetail.getGoodsName() + "(" + orderDetail.getLpn() + ")")
          .setDescription(desc)
          .setDrawable(drawable)
          .endConfig()
          .build();
      unCargoDetailView.addCard(card);
    }
  }
}
