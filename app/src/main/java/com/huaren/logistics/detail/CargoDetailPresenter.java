package com.huaren.logistics.detail;

import android.content.Context;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.SmallImageCardProvider;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.R;
import com.huaren.logistics.bean.Goods;
import com.huaren.logistics.dao.GoodsDao;
import com.huaren.logistics.util.CommonTool;
import com.huaren.logistics.util.StringTool;
import com.huaren.logistics.util.UiTool;
import de.greenrobot.dao.query.QueryBuilder;
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

  public void initCargoDetail(long customerId) {
    GoodsDao goodsDao = LogisticsApplication.getInstance().getDaoSession().getGoodsDao();
    List<Goods> goodsList =
        goodsDao.queryBuilder().where(GoodsDao.Properties.CustomerId.eq(customerId)).list();
    int loadGount = countLoadGoods(goodsList);
    int unLoadCount = goodsList.size() - loadGount;
    cargoDetailView.setLoadInfo(loadGount, unLoadCount);
    for (int i = 0; i < goodsList.size(); i++) {
      Goods goods = goodsList.get(i);
      int drawable = R.drawable.star_do;
      String desc = "未装车";
      if (goods.getIsLoad()) {
        drawable = R.drawable.star_finish;
        desc = "已装车";
      }
      Card card = new Card.Builder((Context) cargoDetailView).setTag(goods.getId())
          .withProvider(SmallImageCardProvider.class)
          .setTitle(goods.getGoodsName() + "(" + goods.getBarCode() + ")")
          .setDescription(desc)
          .setDrawable(drawable)
          .endConfig()
          .build();
      cargoDetailView.addCard(card);
    }
  }

  private int countLoadGoods(List<Goods> goodsList) {
    int count = 0;
    for (int i = 0; i < goodsList.size(); i++) {
      Goods goods = goodsList.get(i);
      if (goods.getIsLoad()) {
        count++;
      }
    }
    return count;
  }

  public void loadCargo(long customerId, String loadCode) {
    if (StringTool.isNotNull(loadCode)) {
      GoodsDao goodsDao = LogisticsApplication.getInstance().getDaoSession().getGoodsDao();
      QueryBuilder qb = goodsDao.queryBuilder();
      qb.where(GoodsDao.Properties.CustomerId.eq(customerId),
          GoodsDao.Properties.BarCode.eq(loadCode));
      List<Goods> goodsList = qb.list();
      if (goodsList != null && !goodsList.isEmpty()) {
        Goods goods = goodsList.get(0);
        if (!goods.getIsLoad()) {
          cargoDetailView.showLoadDialog("装车", "是否装车？");
        } else {
          UiTool.showToast((Context) cargoDetailView, "该货物已经装车！");
        }
      } else {
        qb = goodsDao.queryBuilder();
        qb.where(GoodsDao.Properties.BarCode.eq(loadCode));
        List<Goods> otherGoodsList = qb.list();
        if (otherGoodsList != null && !otherGoodsList.isEmpty()) {
          Goods goods = otherGoodsList.get(0);
          if (!goods.getIsLoad()) {
            cargoDetailView.showLoadDialog("装车", "当前客户"
                + customerId
                + "，未装车数量"
                + cargoDetailView.getUnLoadCount()
                + "，是否切换到客户"
                + goods.getCustomerId());
          } else {
            UiTool.showToast((Context) cargoDetailView, "该货物已经装车！");
          }
        } else {
          UiTool.showToast((Context) cargoDetailView, "货物不存在！");
        }
      }
    } else {
      UiTool.showToast((Context) cargoDetailView, "条码不能为空！");
    }
  }

  public void updateLoadCargo(long customerId, String loadCode) {
    GoodsDao goodsDao = LogisticsApplication.getInstance().getDaoSession().getGoodsDao();
    QueryBuilder qb = goodsDao.queryBuilder();
    qb.where(GoodsDao.Properties.CustomerId.eq(customerId),
        GoodsDao.Properties.BarCode.eq(loadCode));
    List<Goods> goodsList = qb.list();
    if (goodsList != null && !goodsList.isEmpty()) {
      Goods goods = goodsList.get(0);
      goods.setIsLoad(true);
      goodsDao.insertOrReplaceInTx(goods);
      cargoDetailView.reInit();
    }
  }
}
