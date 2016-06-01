package com.huaren.logistics.cargo;

import com.dexafree.materialList.card.Card;

public interface ICargoOrderView {
  void addCard(Card card);

  void showLoadDialog(String title, String message);

  void reInit();

  /**
   * 清空输入框
   */
  void clearLoadText();
}
