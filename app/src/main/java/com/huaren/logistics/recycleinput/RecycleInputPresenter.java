package com.huaren.logistics.recycleinput;

import android.content.Context;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.CargoOrderCardProvider;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.bean.OrderBatch;
import com.huaren.logistics.bean.RecycleInput;
import com.huaren.logistics.dao.OrderBatchDao;
import com.huaren.logistics.dao.RecycleInputDao;
import com.huaren.logistics.util.CommonTool;

import java.util.List;

public class RecycleInputPresenter {
  private IRecycleInputView evaluationView;

  public RecycleInputPresenter(IRecycleInputView evaluationView) {
    this.evaluationView = evaluationView;
  }

  public void initCargoList() {
    OrderBatchDao orderBatchDao = LogisticsApplication.getInstance().getOrderBatchDao();
    String userName = CommonTool.getSharePreference((Context) evaluationView, "curUserName");
    List<OrderBatch> orderBatchList = orderBatchDao.queryBuilder()
        .where(OrderBatchDao.Properties.Status.eq("1"),
            OrderBatchDao.Properties.CanEvalutaion.eq("1"), OrderBatchDao.Properties.UserName.eq(userName))
        .list();
    for (int i = 0; i < orderBatchList.size(); i++) {
      OrderBatch orderBatch = orderBatchList.get(i);
      String desc = "回收货物";
      RecycleInputDao recycleInputDao = LogisticsApplication.getInstance().getRecycleInputDao();
      List<RecycleInput> recycleInputList = recycleInputDao.queryBuilder()
          .where(RecycleInputDao.Properties.OrderBatchId.eq(orderBatch.getId()), RecycleInputDao.Properties.UserName.eq(userName))
          .list();
      int num = 0;
      if (recycleInputList != null && !recycleInputList.isEmpty()) {
        for (int j = 0; j < recycleInputList.size(); j++) {
          RecycleInput recycleInput = recycleInputList.get(j);
          num += recycleInput.getRecycleNum();
        }
      }
      desc += num + "件";
      Card card = new Card.Builder((Context) evaluationView).setTag(orderBatch.getId())
          .withProvider(CargoOrderCardProvider.class)
          .setTitle(orderBatch.getSPdtgCustfullname())
          .setDescription(desc)
          .endConfig()
          .build();
      evaluationView.addCard(card);
    }
  }
}
