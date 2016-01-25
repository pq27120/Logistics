package com.huaren.logistics.uncargo;

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

public class UnCargoPresenter {

  private IUnCargoView unCargoView;

  public UnCargoPresenter(IUnCargoView unCargoView) {
    this.unCargoView = unCargoView;
  }

  public void initUserInfo() {
    String name = CommonTool.getSharePreference((Context) unCargoView, "name");
    String driver = CommonTool.getSharePreference((Context) unCargoView, "name");
    String licensePlate = CommonTool.getSharePreference((Context) unCargoView, "licensePlate");
    unCargoView.setUserInfo(name, driver, licensePlate);
  }

  public void initCargoList() {
    CustomerDao customerDao = LogisticsApplication.getInstance().getDaoSession().getCustomerDao();
    GoodsDao goodsDao = LogisticsApplication.getInstance().getDaoSession().getGoodsDao();
    List<Customer> customerList = customerDao.loadAll();
    for (int i = 0; i < customerList.size(); i++) {
      Customer customer = customerList.get(i);
      List<Goods> goodsList = goodsDao.queryBuilder().where(GoodsDao.Properties.CustomerId.eq(customer.getId())
          , GoodsDao.Properties.IsLoad.eq(true))
          .list();
      int loadGount = countRemoveLoadGoods(goodsList);
      int unLoadCount = goodsList.size() - loadGount;
      String desc = "未卸车：" + unLoadCount + "，已卸车：" + loadGount;
      int drawable = R.drawable.star_do;
      if (unLoadCount == 0) {
        drawable = R.drawable.star_finish;
      }
      Card card = new Card.Builder((Context)unCargoView)
          .setTag(customer.getId())
          .withProvider(SmallImageCardProvider.class)
          .setTitle(customer.getName() + "(" + customer.getCode() +")")
          .setDescription(desc)
          .setDrawable(drawable)
          .endConfig()
          .build();
      unCargoView.addCard(card);
    }
  }

  private int countRemoveLoadGoods(List<Goods> goodsList) {
    int count = 0;
    for (int i = 0; i < goodsList.size(); i++) {
      Goods goods = goodsList.get(i);
      if (goods.getIsRemove()) {
        count ++;
      }
    }
    return count;
  }
}
