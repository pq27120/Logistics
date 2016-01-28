package com.huaren.logistics.cargo;

import android.content.Context;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.SmallImageCardProvider;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.R;
import com.huaren.logistics.bean.OrderDetail;
import com.huaren.logistics.dao.OrderDetailDao;
import com.huaren.logistics.util.CommonTool;
import java.util.List;

public class CargoDetailPresenter {
  private ICargoDetailView cargoDetailView;

  public CargoDetailPresenter(ICargoDetailView cargoDetailView) {
    this.cargoDetailView = cargoDetailView;
  }

  public void initUserInfo() {
    String name = CommonTool.getSharePreference((Context) cargoDetailView, "name");
    String driver = CommonTool.getSharePreference((Context) cargoDetailView, "name");
    String licensePlate = CommonTool.getSharePreference((Context) cargoDetailView, "licensePlate");
    cargoDetailView.setUserInfo(name, driver, licensePlate);
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
      if ("1".equals(orderDetail.getDetailStatus())) {
        drawable = R.drawable.star_do;
        desc = "未装车";
      } else {
        drawable = R.drawable.star_finish;
        desc = "已装车";
      }
      Card card = new Card.Builder((Context) cargoDetailView).setTag(orderDetail.getDetailId())
          .withProvider(SmallImageCardProvider.class)
          .setTitle(orderDetail.getDetailName() + "(" + orderDetail.getDetailId() + ")")
          .setDescription(desc)
          .setDrawable(drawable)
          .endConfig()
          .build();
      cargoDetailView.addCard(card);
    }
  }
}
