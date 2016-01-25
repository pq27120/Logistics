package com.huaren.logistics.undetail;

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

public class UnCargoDetailPresenter{

  private IUnCargoDetailView unCargoDetailView;

  public UnCargoDetailPresenter(IUnCargoDetailView unCargoDetailView) {
    this.unCargoDetailView = unCargoDetailView;
  }

  public void initUserInfo() {
    String name = CommonTool.getSharePreference((Context) unCargoDetailView, "name");
    String driver = CommonTool.getSharePreference((Context) unCargoDetailView, "name");
    String licensePlate = CommonTool.getSharePreference((Context) unCargoDetailView, "licensePlate");
    unCargoDetailView.setUserInfo(name, driver, licensePlate);
  }

  public void initUnCargoDetail(long customerId) {
    GoodsDao goodsDao = LogisticsApplication.getInstance().getDaoSession().getGoodsDao();
    List<Goods> goodsList =
        goodsDao.queryBuilder().where(GoodsDao.Properties.CustomerId.eq(customerId)).list();
    int loadGount = countRemoveGoods(goodsList);
    int unLoadCount = goodsList.size() - loadGount;
    unCargoDetailView.setLoadInfo(loadGount, unLoadCount);
    for (int i = 0; i < goodsList.size(); i++) {
      Goods goods = goodsList.get(i);
      int drawable = R.drawable.star_do;
      String desc = "未卸车";
      if (goods.getIsRemove()) {
        drawable = R.drawable.star_finish;
        desc = "已卸车";
      }
      Card card = new Card.Builder((Context) unCargoDetailView).setTag(goods.getId())
          .withProvider(SmallImageCardProvider.class)
          .setTitle(goods.getGoodsName() + "(" + goods.getBarCode() + ")")
          .setDescription(desc)
          .setDrawable(drawable)
          .endConfig()
          .build();
      unCargoDetailView.addCard(card);
    }
  }

  private int countRemoveGoods(List<Goods> goodsList) {
    int count = 0;
    for (int i = 0; i < goodsList.size(); i++) {
      Goods goods = goodsList.get(i);
      if (goods.getIsRemove()) {
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
        if (!goods.getIsRemove()) {
          unCargoDetailView.showLoadDialog("卸车", "是否卸车？");
        } else {
          UiTool.showToast((Context) unCargoDetailView, "该货物已经卸车！");
        }
      } else {
        qb = goodsDao.queryBuilder();
        qb.where(GoodsDao.Properties.BarCode.eq(loadCode));
        List<Goods> otherGoodsList = qb.list();
        if (otherGoodsList != null && !otherGoodsList.isEmpty()) {
          Goods goods = otherGoodsList.get(0);
          if (!goods.getIsRemove()) {
            unCargoDetailView.showLoadDialog("卸车", "当前客户"
                + customerId
                + "，未卸车数量"
                + unCargoDetailView.getUnLoadCount()
                + "，是否切换到客户"
                + goods.getCustomerId());
          } else {
            UiTool.showToast((Context) unCargoDetailView, "该货物已经卸车！");
          }
        } else {
          UiTool.showToast((Context) unCargoDetailView, "货物不存在！");
        }
      }
    } else {
      UiTool.showToast((Context) unCargoDetailView, "条码不能为空！");
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
      goods.setIsRemove(true);
      goodsDao.insertOrReplaceInTx(goods);
      unCargoDetailView.reInit();
    }
  }
}
