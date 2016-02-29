package com.huaren.logistics.recycleinput;

import android.content.Context;
import android.text.TextUtils;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.bean.RecycleInput;
import com.huaren.logistics.dao.RecycleInputDao;
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

  public void recycleGoods(String customId, String inputNum) {
    if (TextUtils.isEmpty(inputNum)) {
      UiTool.showToast((Context) recycleInputDetailInputView, "请输入需要回收的货物件数！");
      return;
    } else if (!inputNum.matches("[0-9]+")) {
      UiTool.showToast((Context) recycleInputDetailInputView, "货物件数必须为整数！");
      return;
    }
    QueryBuilder qb = recycleInputDao.queryBuilder();
    qb.where(RecycleInputDao.Properties.CooperateId.eq(customId));
    List<RecycleInput> recycleInputList = qb.list();
    if (recycleInputList != null && !recycleInputList.isEmpty()) {
      RecycleInput recycleInput = recycleInputList.get(0);
      recycleInputDetailInputView.showDialog("回收录入", "该用户已经录入过回收数据，确认覆盖？", recycleInput);
    } else {
      insertRecycleInput(customId, inputNum);
      UiTool.showToast((Context) recycleInputDetailInputView, "回收录入新增完成！");
      recycleInputDetailInputView.enterRecycleInput();
    }
  }

  private void insertRecycleInput(String customId, String inputNum) {
    RecycleInput recycleInput = new RecycleInput();
    recycleInput.setCooperateId(customId);
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
    UiTool.showToast((Context) recycleInputDetailInputView, "回收录入修改完成！");
    recycleInputDetailInputView.enterRecycleInput();
  }
}
