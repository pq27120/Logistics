package com.huaren.logistics.recyclescan;

import android.content.Context;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.SmallImageCardProvider;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.bean.RecycleInput;
import com.huaren.logistics.bean.RecycleScan;
import com.huaren.logistics.dao.RecycleInputDao;
import com.huaren.logistics.dao.RecycleScanDao;
import com.huaren.logistics.util.UiTool;
import de.greenrobot.dao.query.QueryBuilder;
import java.util.Date;
import java.util.List;

public class RecycleScanDetailPresent {
  private IRecycleScanDetailView recycleScanDetailView;
  private RecycleScanDao recycleScanDao = LogisticsApplication.getInstance().getRecycleScanDao();

  public RecycleScanDetailPresent(IRecycleScanDetailView recycleScanDetailView) {
    this.recycleScanDetailView = recycleScanDetailView;
  }

  public void recycleGoods(String customerId, String scanCode) {
    QueryBuilder qb = recycleScanDao.queryBuilder();
    qb.where(RecycleScanDao.Properties.ScanCode.eq(scanCode));
    List<RecycleScan> recycleScanList = qb.list();
    if (recycleScanList != null && !recycleScanList.isEmpty()) {
      UiTool.showToast((Context)recycleScanDetailView , "已经扫描过该货物！");
    } else {
      insertRecycleScan(customerId, scanCode);
      UiTool.showToast((Context) recycleScanDetailView, "回收录入扫描完成！");
      recycleScanDetailView.enterRecycleScan();
    }
  }

  private void insertRecycleScan(String customerId, String scanCode) {
    RecycleInputDao recycleInputDao = LogisticsApplication.getInstance().getRecycleInputDao();
    Long inputId = 0l;
    List<RecycleInput> recycleInputList = recycleInputDao.queryBuilder()
        .where(RecycleInputDao.Properties.CooperateId.eq(customerId))
        .list();
    if (recycleInputList != null && !recycleInputList.isEmpty()) {
      inputId = recycleInputList.get(0).getId();
    }
    RecycleScan recycleScan = new RecycleScan();
    recycleScan.setScanCode(scanCode);
    recycleScan.setInputId(inputId);
    recycleScan.setRecycleScanTime(new Date());
    recycleScanDao.insert(recycleScan);
  }

  public void initRecycleScanList(String customerId) {
    RecycleInputDao recycleInputDao = LogisticsApplication.getInstance().getRecycleInputDao();
    RecycleInput recycleInput =
        recycleInputDao.queryBuilder().where(RecycleInputDao.Properties.CooperateId.eq(customerId)).unique();
    if (recycleInput != null) {
      RecycleScanDao recycleScanDao = LogisticsApplication.getInstance().getRecycleScanDao();
      List<RecycleScan> recycleScanList = recycleScanDao.queryBuilder()
          .where(RecycleScanDao.Properties.InputId.eq(recycleInput.getId())).list();
      for(int i=0;i<recycleScanList.size();i++) {
        RecycleScan recycleScan = recycleScanList.get(i);
      Card card = new Card.Builder((Context) recycleScanDetailView).setTag(recycleScan.getScanCode())
          .withProvider(SmallImageCardProvider.class)
          .setTitle(recycleScan.getScanCode())
          .setDescription("")
          .endConfig()
          .build();
      recycleScanDetailView.addCard(card);
      }
    }
  }
}
