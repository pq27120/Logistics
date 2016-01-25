package com.huaren.logistics.uncargo;

import com.dexafree.materialList.card.Card;

public interface IUnCargoView {
  void setUserInfo(String name, String driver, String licensePlate);

  void addCard(Card card);
}
