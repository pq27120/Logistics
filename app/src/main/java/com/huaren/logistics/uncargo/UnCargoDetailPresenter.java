package com.huaren.logistics.uncargo;

import android.content.Context;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.SmallImageCardProvider;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.R;
import com.huaren.logistics.bean.OrderDetail;
import com.huaren.logistics.dao.OrderDetailDao;
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
        orderDetailDao.queryBuilder().where(OrderDetailDao.Properties.OrderId.eq(orderId)).list();
    for (int i = 0; i < orderDetailList.size(); i++) {
      OrderDetail orderDetail = orderDetailList.get(i);
      int drawable;
      String desc;
      if ("2".equals(orderDetail.getDetailStatus())) {
        drawable = R.drawable.star_do;
        desc = "未卸车";
      } else {
        drawable = R.drawable.star_finish;
        desc = "已卸车";
      }
      Card card = new Card.Builder((Context) unCargoDetailView).setTag(orderDetail.getDetailId())
          .withProvider(SmallImageCardProvider.class)
          .setTitle(orderDetail.getDetailName() + "(" + orderDetail.getDetailId() + ")")
          .setDescription(desc)
          .setDrawable(drawable)
          .endConfig()
          .build();
      unCargoDetailView.addCard(card);
    }
  }
}
