package com.huaren.logistics.recyclescan;

import android.content.Context;
import android.text.TextUtils;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.SmallImageCardProvider;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.bean.RecycleScan;
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

  public void recycleGoods(String scanCode) {
    if (TextUtils.isEmpty(scanCode)) {
      LogisticsApplication.getInstance().getSoundPoolUtil().playWrong();
      UiTool.showToast((Context) recycleScanDetailView, "请输入条码！");
    } else if (!scanCode.startsWith("A")) {
      LogisticsApplication.getInstance().getSoundPoolUtil().playWrong();
      UiTool.showToast((Context) recycleScanDetailView, "条码必须A开头！");
    } else if (scanCode.length() != 8) {
      LogisticsApplication.getInstance().getSoundPoolUtil().playWrong();
      UiTool.showToast((Context) recycleScanDetailView, "条码只能为8位！");
    } else {
      QueryBuilder qb = recycleScanDao.queryBuilder();
      qb.where(RecycleScanDao.Properties.ScanCode.eq(scanCode));
      List<RecycleScan> recycleScanList = qb.list();
      if (recycleScanList != null && !recycleScanList.isEmpty()) {
        LogisticsApplication.getInstance().getSoundPoolUtil().playWrong();
        UiTool.showToast((Context) recycleScanDetailView, "已经扫描过该货物！");
      } else {
        insertRecycleScan(scanCode);
        recycleScanDetailView.init();
      }
    }
  }

  private void insertRecycleScan(String scanCode) {
    RecycleScan recycleScan = new RecycleScan();
    recycleScan.setStatus("1");
    recycleScan.setScanCode(scanCode);
    recycleScan.setRecycleScanTime(new Date());
    recycleScanDao.insert(recycleScan);
    LogisticsApplication.getInstance().getSoundPoolUtil().playRight();
    UiTool.showToast((Context) recycleScanDetailView, "回收录入扫描完成！");
  }

  public void initRecycleScanList() {
    RecycleScanDao recycleScanDao = LogisticsApplication.getInstance().getRecycleScanDao();
    List<RecycleScan> recycleScanList = recycleScanDao.queryBuilder().orderDesc(RecycleScanDao.Properties.RecycleScanTime).list();
    for (int i = 0; i < recycleScanList.size(); i++) {
      RecycleScan recycleScan = recycleScanList.get(i);
      Card card =
          new Card.Builder((Context) recycleScanDetailView).setTag(recycleScan.getScanCode())
              .withProvider(SmallImageCardProvider.class)
              .setTitle(recycleScan.getScanCode())
              .setDescription("")
              .endConfig()
              .build();
      recycleScanDetailView.addCard(card);
    }
  }
}
