package com.huaren.logistics.recyclescan;

import com.dexafree.materialList.card.Card;

public interface IRecycleScanDetailView {
  void addCard(Card card);

  /**
   * 初始化
   */
  void init();
}
