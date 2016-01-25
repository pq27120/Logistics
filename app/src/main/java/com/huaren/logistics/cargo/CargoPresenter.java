package com.huaren.logistics.cargo;

import android.content.Context;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.SmallImageCardProvider;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.R;
import com.huaren.logistics.bean.Customer;
import com.huaren.logistics.bean.Goods;
import com.huaren.logistics.dao.CustomerDao;
import com.huaren.logistics.dao.GoodsDao;
import com.huaren.logistics.util.CommonTool;
import java.util.List;

public class CargoPresenter {

  private ICargoView cargoView;

  public CargoPresenter(ICargoView cargoView) {
    this.cargoView = cargoView;
  }

  public void initUserInfo() {
    String name = CommonTool.getSharePreference((Context) cargoView, "name");
    String driver = CommonTool.getSharePreference((Context) cargoView, "name");
    String licensePlate = CommonTool.getSharePreference((Context) cargoView, "licensePlate");
    cargoView.setUserInfo(name, driver, licensePlate);
  }

  public void initCargoList() {
    CustomerDao customerDao = LogisticsApplication.getInstance().getDaoSession().getCustomerDao();
    GoodsDao goodsDao = LogisticsApplication.getInstance().getDaoSession().getGoodsDao();
    List<Customer> customerList = customerDao.loadAll();
    for (int i = 0; i < customerList.size(); i++) {
      Customer customer = customerList.get(i);
      List<Goods> goodsList = goodsDao.queryBuilder().where(GoodsDao.Properties.CustomerId.eq(customer.getId()))
          .list();
      int loadGount = countLoadGoods(goodsList);
      int unLoadCount = goodsList.size() - loadGount;
      String desc = "未装车：" + unLoadCount + "，已装车：" + loadGount;
      int drawable = R.drawable.star_do;
      if (unLoadCount == 0) {
        drawable = R.drawable.star_finish;
      }
      Card card = new Card.Builder((Context)cargoView)
          .setTag(customer.getId())
          .withProvider(SmallImageCardProvider.class)
          .setTitle(customer.getName() + "(" + customer.getCode() +")")
          .setDescription(desc)
          .setDrawable(drawable)
          .endConfig()
          .build();
      cargoView.addCard(card);
    }
  }

  private int countLoadGoods(List<Goods> goodsList) {
    int count = 0;
    for (int i = 0; i < goodsList.size(); i++) {
      Goods goods = goodsList.get(i);
      if (goods.getIsLoad()) {
        count ++;
      }
    }
    return count;
  }
}
