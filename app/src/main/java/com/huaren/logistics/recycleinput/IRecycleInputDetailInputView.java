package com.huaren.logistics.recycleinput;

import com.dexafree.materialList.card.Card;
import com.huaren.logistics.bean.RecycleInput;
import com.huaren.logistics.bean.SysDicValue;

import java.util.List;

public interface IRecycleInputDetailInputView {
  void showDialog(String title, String message, RecycleInput recycleInput);

  void addCard(Card card);

  void init();

  /**
   * 初始化单选列表
   * @param list
   */
  void initRadio(List<SysDicValue> list);
}
