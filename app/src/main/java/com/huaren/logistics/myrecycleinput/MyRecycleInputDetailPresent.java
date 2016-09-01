package com.huaren.logistics.myrecycleinput;

import android.content.Context;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.CargoOrderCardProvider;
import com.dexafree.materialList.card.provider.CargoSingleCardProvider;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.bean.RecycleInput;
import com.huaren.logistics.dao.RecycleInputDao;
import com.huaren.logistics.util.CommonTool;
import com.huaren.logistics.util.DateUtil;

import java.util.List;

/**
 * Created by bj on 2016/9/1.
 */
public class MyRecycleInputDetailPresent {
    private IMyRecycleInputDetailView recycleInputDetailView;

    public MyRecycleInputDetailPresent(IMyRecycleInputDetailView recycleInputDetailView) {
        this.recycleInputDetailView = recycleInputDetailView;
    }

    public void initRecycleInputList(String orderBatchId) {
        RecycleInputDao recycleInputDao = LogisticsApplication.getInstance().getRecycleInputDao();
        String userName = CommonTool.getSharePreference((Context) recycleInputDetailView, "curUserName");
        if("historyRecycle".equals(orderBatchId)){
            List<RecycleInput> recycleInputList = recycleInputDao.queryBuilder()
                    .where(RecycleInputDao.Properties.UserName.eq(userName), RecycleInputDao.Properties.Status.notEq("1"))
                    .orderDesc(RecycleInputDao.Properties.RecycleTime)
                    .list();
            for (int i = 0; i < recycleInputList.size(); i++) {
                RecycleInput recycleInput = recycleInputList.get(i);
                String desc = recycleInput.getUpStatus().equals("1") ? "已上传" : "未上传";
                Card card =
                        new Card.Builder((Context) recycleInputDetailView).setTag("" + recycleInput.getId())
                                .withProvider(CargoOrderCardProvider.class)
                                .setTitle(recycleInput.getLPdtgBatch() + " " + recycleInput.getCooperateId() + "(" + desc + ")")
                                .setDescription(recycleInput.getRecycleTypeValue() + "(" + recycleInput.getRecycleNum() + "件) " + DateUtil.parseDateToString(recycleInput.getRecycleTime(), DateUtil.DATE_TIME_FORMATE))
                                        .endConfig()
                                .build();
                recycleInputDetailView.addCard(card);
            }
        }else {
            List<RecycleInput> recycleInputList = recycleInputDao.queryBuilder()
                    .where(RecycleInputDao.Properties.OrderBatchId.eq(orderBatchId) ,RecycleInputDao.Properties.Status.eq("1"), RecycleInputDao.Properties.UserName.eq(userName))
                    .orderDesc(RecycleInputDao.Properties.RecycleTime)
                    .list();
            for (int i = 0; i < recycleInputList.size(); i++) {
                RecycleInput recycleInput = recycleInputList.get(i);
                String desc = recycleInput.getUpStatus().equals("1") ? "已上传" : "未上传";
                Card card =
                        new Card.Builder((Context) recycleInputDetailView).setTag("" + recycleInput.getId())
                                .withProvider(CargoSingleCardProvider.class)
                                .setTitle(recycleInput.getLPdtgBatch() + " " + recycleInput.getRecycleTypeValue() + "(" + recycleInput.getRecycleNum() + "件  " + desc + ")")
                                .setDescription("")
                                .endConfig()
                                .build();
                recycleInputDetailView.addCard(card);
            }
        }
    }
}
