package com.huaren.logistics.uncargo;

import com.dexafree.materialList.card.Card;

public interface IUnCargoOrderView {
  void addCard(Card card);

  void showLoadDialog(String title, String message);

  void reInit();

  /**
   * 直接进入评价页面
   * @param customerId 客户ID
   * @param orderId 订单ID
   */
  void enterApprovalView(String customerId, String orderId);
}
