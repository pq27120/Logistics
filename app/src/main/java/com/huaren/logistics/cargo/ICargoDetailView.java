package com.huaren.logistics.cargo;

import com.dexafree.materialList.card.Card;

public interface ICargoDetailView {
  void setUserInfo(String name, String driver, String licensePlate);

  void addCard(Card card);

  void showLoadDialog(String customerId, String barCode);

  void reInit();
}
