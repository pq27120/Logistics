package com.huaren.logistics.evaluation;

import android.content.Context;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.SmallImageCardProvider;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.R;
import com.huaren.logistics.bean.Customer;
import com.huaren.logistics.dao.CustomerDao;
import com.huaren.logistics.util.CommonTool;
import java.util.List;

public class EvaluationPresenter {
  private IEvaluationView evaluationView;

  public EvaluationPresenter(IEvaluationView evaluationView) {
    this.evaluationView = evaluationView;
  }

  public void initUserInfo() {
    String name = CommonTool.getSharePreference((Context) evaluationView, "name");
    String driver = CommonTool.getSharePreference((Context) evaluationView, "name");
    String licensePlate = CommonTool.getSharePreference((Context) evaluationView, "licensePlate");
    evaluationView.setUserInfo(name, driver, licensePlate);
  }

  public void initCargoList() {
    CustomerDao customerDao = LogisticsApplication.getInstance().getDaoSession().getCustomerDao();
    //GoodsDao goodsDao = LogisticsApplication.getInstance().getDaoSession().getGoodsDao();
    List<Customer> customerList = customerDao.loadAll();
    for (int i = 0; i < customerList.size(); i++) {
      Customer customer = customerList.get(i);
      //List<Goods> goodsList = goodsDao.queryBuilder().where(GoodsDao.Properties.CustomerId.eq(customer.getId())
      //    , GoodsDao.Properties.IsLoad.eq(true))
      //    .list();
      //int loadGount = countRemoveLoadGoods(goodsList);
      //int unLoadCount = goodsList.size() - loadGount;
      //String desc = "未卸车：" + unLoadCount + "，已卸车：" + loadGount;
      int drawable = R.drawable.star_do;
      //if (unLoadCount == 0) {
      //  drawable = R.drawable.star_finish;
      //}
      //Card card = new Card.Builder((Context)evaluationView)
      //    .setTag(customer.getId())
      //    .withProvider(SmallImageCardProvider.class)
      //    .setTitle(customer.getName() + "(" + customer.getCode() +")")
      //    .setDescription(desc)
      //    .setDrawable(drawable)
      //    .endConfig()
      //    .build();
      //evaluationView.addCard(card);
    }
  }

  //private int countRemoveLoadGoods(List<Goods> goodsList) {
  //  int count = 0;
  //  for (int i = 0; i < goodsList.size(); i++) {
  //    Goods goods = goodsList.get(i);
  //    if (goods.getIsRemove()) {
  //      count ++;
  //    }
  //  }
  //  return count;
  //}
}
