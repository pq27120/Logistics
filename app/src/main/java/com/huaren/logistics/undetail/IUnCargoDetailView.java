package com.huaren.logistics.undetail;

import com.dexafree.materialList.card.Card;

public interface IUnCargoDetailView {
  void setUserInfo(String name, String driver, String licensePlate);

  void addCard(Card card);

  void setLoadInfo(int loadGount, int unLoadCount);

  void showLoadDialog(String customerId, String barCode);

  void reInit();

  String getUnLoadCount();
}
