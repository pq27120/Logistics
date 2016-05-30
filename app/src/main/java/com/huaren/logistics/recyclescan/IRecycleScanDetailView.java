package com.huaren.logistics.recyclescan;

import com.dexafree.materialList.card.Card;
import com.huaren.logistics.bean.SysDicValue;

import java.util.List;

public interface IRecycleScanDetailView {
  void addCard(Card card);

  /**
   * 初始化
   */
  void init();

  void initRadio(List<SysDicValue> list);

  /**
   * 当前回收扫描的数目
   * @param size
   */
  void setRecycleNum(int size);
}
