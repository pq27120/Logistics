package com.huaren.logistics.recycleinput;

import android.content.Context;
import android.text.TextUtils;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.CargoSingleCardProvider;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.bean.OrderBatch;
import com.huaren.logistics.bean.RecycleInput;
import com.huaren.logistics.bean.SysDicValue;
import com.huaren.logistics.dao.OrderBatchDao;
import com.huaren.logistics.dao.RecycleInputDao;
import com.huaren.logistics.dao.SysDicValueDao;
import com.huaren.logistics.util.CommonTool;
import com.huaren.logistics.util.UiTool;

import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

public class RecycleInputDetailPresent {
  private IRecycleInputDetailInputView recycleInputDetailInputView;
  private RecycleInputDao recycleInputDao = LogisticsApplication.getInstance().getRecycleInputDao();

  public RecycleInputDetailPresent(IRecycleInputDetailInputView recycleInputDetailInputView) {
    this.recycleInputDetailInputView = recycleInputDetailInputView;
  }

  public void recycleGoods(String orderBatchId, SysDicValue sysDicValue, String inputNum) {
    if (sysDicValue == null) {
      LogisticsApplication.getInstance().getSoundPoolUtil().playWrong();
      UiTool.showToast((Context) recycleInputDetailInputView, "请选择回收类型！");
      return;
    }
    if (TextUtils.isEmpty(inputNum)) {
      LogisticsApplication.getInstance().getSoundPoolUtil().playWrong();
      UiTool.showToast((Context) recycleInputDetailInputView, "请输入需要回收的货物件数！");
      return;
    } else if (!inputNum.matches("[0-9]+")) {
      LogisticsApplication.getInstance().getSoundPoolUtil().playWrong();
      UiTool.showToast((Context) recycleInputDetailInputView, "货物件数必须为整数！");
      return;
    }
    String userName = CommonTool.getSharePreference((Context) recycleInputDetailInputView, "curUserName");
    RecycleInput recycleInput = recycleInputDao.queryBuilder()
        .where(RecycleInputDao.Properties.OrderBatchId.eq(orderBatchId),
            RecycleInputDao.Properties.RecycleType.eq(sysDicValue.getId()),
            RecycleInputDao.Properties.Status.eq("1"), RecycleInputDao.Properties.UserName.eq(userName)).unique();
    if (recycleInput != null) {
      recycleInputDetailInputView.showDialog("回收录入", "已经录入过回收数据，确认覆盖？", recycleInput);
    } else {
      insertRecycleInput(orderBatchId, sysDicValue, inputNum);
      LogisticsApplication.getInstance().getSoundPoolUtil().playRight();
      UiTool.showToast((Context) recycleInputDetailInputView, "回收录入新增完成！");
      recycleInputDetailInputView.init();
    }
  }

  private void insertRecycleInput(String orderBatchId, SysDicValue sysDicValue, String inputNum) {
    String userName = CommonTool.getSharePreference((Context) recycleInputDetailInputView, "curUserName");
    RecycleInput recycleInput = new RecycleInput();
    recycleInput.setRecycleNum(Integer.valueOf(inputNum));
    recycleInput.setRecycleTime(new Date());
    recycleInput.setStatus("1");
    recycleInput.setUserName(userName);
    recycleInput.setOrderBatchId(orderBatchId);
    recycleInput.setRecycleType(sysDicValue.getId());
    recycleInput.setRecycleTypeValue(sysDicValue.getMyDisplayValue());
    OrderBatch orderBatch = LogisticsApplication.getInstance().getOrderBatchDao().queryBuilder().where(
        OrderBatchDao.Properties.Id.eq(orderBatchId), OrderBatchDao.Properties.Status.eq("1"), OrderBatchDao.Properties.UserName.eq(userName)).unique();
    if (orderBatch != null) {
      recycleInput.setCooperateId(orderBatch.getCooperateId());
      recycleInput.setDriversID(orderBatch.getDriversID());
      recycleInput.setLPdtgBatch(orderBatch.getLPdtgBatch());
    }
    recycleInputDao.insert(recycleInput);
  }

  /**
   * 回收数据更新
   */
  public void updateRecycleInput(SysDicValue sysDicValue, String inputNum, RecycleInput recycleInput) {
    recycleInput.setRecycleNum(Integer.valueOf(inputNum));
    recycleInput.setRecycleTime(new Date());
    recycleInput.setRecycleType(sysDicValue.getId());
    recycleInput.setRecycleTypeValue(sysDicValue.getMyDisplayValue());
    recycleInputDao.update(recycleInput);
    LogisticsApplication.getInstance().getSoundPoolUtil().playRight();
    UiTool.showToast((Context) recycleInputDetailInputView, "回收录入修改完成！");
    recycleInputDetailInputView.init();
  }

  public void initRecycleInputList(String orderBatchId) {
    RecycleInputDao recycleInputDao = LogisticsApplication.getInstance().getRecycleInputDao();
    String userName = CommonTool.getSharePreference((Context) recycleInputDetailInputView, "curUserName");
    List<RecycleInput> recycleInputList = recycleInputDao.queryBuilder()
        .where(RecycleInputDao.Properties.OrderBatchId.eq(orderBatchId),
            RecycleInputDao.Properties.Status.eq("1"), RecycleInputDao.Properties.UserName.eq(userName))
        .list();
    for (int i = 0; i < recycleInputList.size(); i++) {
      RecycleInput recycleInput = recycleInputList.get(i);
      Card card =
          new Card.Builder((Context) recycleInputDetailInputView).setTag("" + recycleInput.getId())
              .withProvider(CargoSingleCardProvider.class)
              .setTitle(recycleInput.getRecycleTypeValue() + "(回收：" + recycleInput.getRecycleNum() + "件)")
              .setDescription("")
              .endConfig()
              .build();
      recycleInputDetailInputView.addCard(card);
    }
  }

  public void initRecycleInputRadio() {
    SysDicValueDao sysDicValueDao = LogisticsApplication.getInstance().getSysDicValueDao();
    QueryBuilder qb = sysDicValueDao.queryBuilder();
    List<SysDicValue> list = qb.where(SysDicValueDao.Properties.DicId.eq(23)).list();
    recycleInputDetailInputView.initRadio(list);
  }
}
