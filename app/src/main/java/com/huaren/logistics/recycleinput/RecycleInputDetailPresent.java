package com.huaren.logistics.recycleinput;

import android.content.Context;
import android.text.TextUtils;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.SmallImageCardProvider;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.bean.RecycleInput;
import com.huaren.logistics.dao.RecycleInputDao;
import com.huaren.logistics.util.DateUtil;
import com.huaren.logistics.util.UiTool;
import de.greenrobot.dao.query.QueryBuilder;
import java.util.Date;
import java.util.List;

public class RecycleInputDetailPresent {
  private IRecycleInputDetailInputView recycleInputDetailInputView;
  private RecycleInputDao recycleInputDao = LogisticsApplication.getInstance().getRecycleInputDao();

  public RecycleInputDetailPresent(IRecycleInputDetailInputView recycleInputDetailInputView) {
    this.recycleInputDetailInputView = recycleInputDetailInputView;
  }

  public void recycleGoods(String inputNum) {
    if (TextUtils.isEmpty(inputNum)) {
      LogisticsApplication.getInstance().getSoundPoolUtil().playWrong();
      UiTool.showToast((Context) recycleInputDetailInputView, "请输入需要回收的货物件数！");
      return;
    } else if (!inputNum.matches("[0-9]+")) {
      LogisticsApplication.getInstance().getSoundPoolUtil().playWrong();
      UiTool.showToast((Context) recycleInputDetailInputView, "货物件数必须为整数！");
      return;
    }
    QueryBuilder qb = recycleInputDao.queryBuilder();
    List<RecycleInput> recycleInputList = qb.list();
    if (recycleInputList != null && !recycleInputList.isEmpty()) {
      RecycleInput recycleInput = recycleInputList.get(0);
      recycleInputDetailInputView.showDialog("回收录入", "已经录入过回收数据，确认覆盖？", recycleInput);
    } else {
      insertRecycleInput(inputNum);
      LogisticsApplication.getInstance().getSoundPoolUtil().playRight();
      UiTool.showToast((Context) recycleInputDetailInputView, "回收录入新增完成！");
      recycleInputDetailInputView.init();
    }
  }

  private void insertRecycleInput(String inputNum) {
    RecycleInput recycleInput = new RecycleInput();
    recycleInput.setRecycleNum(Integer.valueOf(inputNum));
    recycleInput.setRecycleTime(new Date());
    recycleInputDao.insert(recycleInput);
  }

  /**
   * 回收数据更新
   */
  public void updateRecycleInput(String inputNum, RecycleInput recycleInput) {
    recycleInput.setRecycleNum(Integer.valueOf(inputNum));
    recycleInput.setRecycleTime(new Date());
    recycleInputDao.update(recycleInput);
    LogisticsApplication.getInstance().getSoundPoolUtil().playRight();
    UiTool.showToast((Context) recycleInputDetailInputView, "回收录入修改完成！");
    recycleInputDetailInputView.init();
  }

  public void initRecycleInputList() {
    RecycleInputDao recycleInputDao = LogisticsApplication.getInstance().getRecycleInputDao();
    List<RecycleInput> recycleInputList = recycleInputDao.queryBuilder().list();
    if (recycleInputList != null && !recycleInputList.isEmpty()) {
      RecycleInput recycleInput = recycleInputList.get(0);
      Card card =
          new Card.Builder((Context) recycleInputDetailInputView).setTag("" + recycleInput.getId())
              .withProvider(SmallImageCardProvider.class)
              .setTitle("回收货物" + recycleInput.getRecycleNum() + "件")
              .setDescription("录入时间：" + DateUtil.parseDateToString(recycleInput.getRecycleTime(), DateUtil.DATE_TIME_FORMATE))
              .endConfig()
              .build();
      recycleInputDetailInputView.addCard(card);
    }
  }
}
