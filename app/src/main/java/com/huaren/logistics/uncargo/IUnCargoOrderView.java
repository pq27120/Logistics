package com.huaren.logistics.uncargo;

import com.dexafree.materialList.card.Card;

public interface IUnCargoOrderView {
  void addCard(Card card);

  void showLoadDialog(String title, String message);

  void reInit();

  /**
   * 销毁Activity
   */
  void finishActivity();

  /**
   * 清空输入框
   */
  void clearRemoveText();
}
