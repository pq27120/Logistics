package com.huaren.logistics.recyclescan;

import android.content.Context;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.SmallImageCardProvider;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.bean.Customer;
import com.huaren.logistics.bean.RecycleInput;
import com.huaren.logistics.dao.CustomerDao;
import com.huaren.logistics.dao.RecycleInputDao;
import java.util.List;

public class RecycleScanPresent {

  private IRecycleScanView recycleScanView;

  public RecycleScanPresent(IRecycleScanView recycleScanView) {
    this.recycleScanView = recycleScanView;
  }

  public void initCustomList() {
    CustomerDao customerDao = LogisticsApplication.getInstance().getCustomerDao();
    RecycleInputDao recycleInputDao = LogisticsApplication.getInstance().getRecycleInputDao();
    List<Customer> customerList = customerDao.loadAll();
    for (int i = 0; i < customerList.size(); i++) {
      Customer customer = customerList.get(i);
      List<RecycleInput> recycleInputList = recycleInputDao.queryBuilder()
          .where(RecycleInputDao.Properties.CustomId.eq(customer.getCustomerId()))
          .list();
      int count = 0;
      if (recycleInputList != null && !recycleInputList.isEmpty()) {
        count = recycleInputList.get(0).getRecycleNum();
      }
      Card card = new Card.Builder((Context) recycleScanView).setTag(customer.getCustomerId())
          .withProvider(SmallImageCardProvider.class)
          .setTitle(customer.getName() + "(" + customer.getCustomerId() + ")")
          .setDescription("回收货物" + count + "件")
          .endConfig()
          .build();
      recycleScanView.addCard(card);
    }
  }
}
