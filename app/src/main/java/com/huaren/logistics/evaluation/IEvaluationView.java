package com.huaren.logistics.evaluation;

import com.dexafree.materialList.card.Card;

public interface IEvaluationView {
  void setUserInfo(String name, String driver, String licensePlate);

  void addCard(Card card);
}