package com.huaren.logistics.myrecycleinput;

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

/**
 * Created by bj on 2016/9/1.
 */
public class MyRecycleInputPresenter {

    private IMyRecycleInputView recycleInputView;

    public MyRecycleInputPresenter(IMyRecycleInputView recycleInputView) {
        this.recycleInputView = recycleInputView;
    }

    public void initCargoList() {
        OrderBatchDao orderBatchDao = LogisticsApplication.getInstance().getOrderBatchDao();
        String userName = CommonTool.getSharePreference((Context) recycleInputView, "curUserName");
        List<OrderBatch> orderBatchList = orderBatchDao.queryBuilder()
                .where(OrderBatchDao.Properties.Status.eq("1"), OrderBatchDao.Properties.UserName.eq(userName))
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
            Card card = new Card.Builder((Context) recycleInputView).setTag(orderBatch.getId())
                    .withProvider(CargoOrderCardProvider.class)
                    .setTitle(orderBatch.getSPdtgCustfullname())
                    .setDescription(desc)
                    .endConfig()
                    .build();
            recycleInputView.addCard(card);
        }
        Card card = new Card.Builder((Context) recycleInputView).setTag("historyRecycle")
                .withProvider(CargoOrderCardProvider.class)
                .setTitle("历史批次回收录入列表")
                .setDescription("")
                .endConfig()
                .build();
        recycleInputView.addCard(card);
    }
}
