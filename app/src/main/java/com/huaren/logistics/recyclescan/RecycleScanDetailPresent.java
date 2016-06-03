package com.huaren.logistics.recyclescan;

import android.content.Context;
import android.text.TextUtils;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.CargoSingleCardProvider;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.bean.RecycleScan;
import com.huaren.logistics.bean.SysDic;
import com.huaren.logistics.bean.SysDicValue;
import com.huaren.logistics.dao.RecycleScanDao;
import com.huaren.logistics.dao.SysDicDao;
import com.huaren.logistics.dao.SysDicValueDao;
import com.huaren.logistics.util.CommonTool;
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

    public void recycleGoods(SysDicValue sysDicValue, String scanCode) {
        if (sysDicValue == null) {
            LogisticsApplication.getInstance().getSoundPoolUtil().playWrong();
            UiTool.showToast((Context) recycleScanDetailView, "请选择回收类型！");
            return;
        }
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
            String userName = CommonTool.getSharePreference((Context) recycleScanDetailView, "curUserName");
            qb.where(RecycleScanDao.Properties.ScanCode.eq(scanCode), RecycleScanDao.Properties.UserName.eq(userName));
            List<RecycleScan> recycleScanList = qb.list();
            if (recycleScanList != null && !recycleScanList.isEmpty()) {
                LogisticsApplication.getInstance().getSoundPoolUtil().playWrong();
                UiTool.showToast((Context) recycleScanDetailView, "已经扫描过该货物！");
            } else {
                insertRecycleScan(sysDicValue, scanCode);
                recycleScanDetailView.init();
            }
        }
    }

    private void insertRecycleScan(SysDicValue sysDicValue, String scanCode) {
        String userName = CommonTool.getSharePreference((Context) recycleScanDetailView, "curUserName");
        RecycleScan recycleScan = new RecycleScan();
        recycleScan.setStatus("1");
        recycleScan.setUserName(userName);
        recycleScan.setScanCode(scanCode);
        recycleScan.setRecycleScanTime(new Date());
        recycleScan.setRecycleType(sysDicValue.getId());
        recycleScan.setRecycleTypeValue(sysDicValue.getMyDisplayValue());
        recycleScanDao.insert(recycleScan);
        LogisticsApplication.getInstance().getSoundPoolUtil().playRight();
        UiTool.showToast((Context) recycleScanDetailView, "回收录入扫描完成！");
    }

    public void initRecycleScanList() {
        RecycleScanDao recycleScanDao = LogisticsApplication.getInstance().getRecycleScanDao();
        String userName = CommonTool.getSharePreference((Context) recycleScanDetailView, "curUserName");
        List<RecycleScan> recycleScanList = recycleScanDao.queryBuilder().where(RecycleScanDao.Properties.UserName.eq(userName)).orderDesc(RecycleScanDao.Properties.RecycleScanTime).list();
        recycleScanDetailView.setRecycleNum(recycleScanList.size());
        for (int i = 0; i < recycleScanList.size(); i++) {
            RecycleScan recycleScan = recycleScanList.get(i);
            Card card =
                    new Card.Builder((Context) recycleScanDetailView).setTag(recycleScan.getScanCode())
                            .withProvider(CargoSingleCardProvider.class)
                            .setTitle(recycleScan.getScanCode() + "(" + recycleScan.getRecycleTypeValue() + ")")
                            .setDescription("")
                            .endConfig()
                            .build();
            recycleScanDetailView.addCard(card);
        }
    }

    public void initRecycleInputRadio() {
        SysDicValueDao sysDicValueDao = LogisticsApplication.getInstance().getSysDicValueDao();
        QueryBuilder qb = sysDicValueDao.queryBuilder();
        SysDicDao sysDicDao = LogisticsApplication.getInstance().getSysDicDao();
        SysDic sysDic = sysDicDao.queryBuilder().where(SysDicDao.Properties.MyName.eq("回收类型")).unique();
        if(sysDic != null) {
            List<SysDicValue> list = qb.where(SysDicValueDao.Properties.DicId.eq(sysDic.getId())).list();
            recycleScanDetailView.initRadio(list);
        }
    }
}
