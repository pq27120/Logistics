package com.huaren.logistics.cargo;

import com.dexafree.materialList.card.Card;

public interface ICargoView {
  void setUserInfo(String name, String driver, String licensePlate);

  void addCard(Card card);
}
