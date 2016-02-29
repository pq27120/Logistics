package com.huaren.logistics.recycleinput;

import android.content.Context;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.SmallImageCardProvider;
import com.huaren.logistics.LogisticsApplication;
import com.huaren.logistics.bean.Customer;
import com.huaren.logistics.dao.CustomerDao;
import java.util.List;

public class RecycleInputPresenter {
  private IRecycleInputView recycleInputView;

  public RecycleInputPresenter(IRecycleInputView recycleInputView) {
    this.recycleInputView = recycleInputView;
  }

  public void initCustomList() {
    CustomerDao customerDao = LogisticsApplication.getInstance().getDaoSession().getCustomerDao();
    List<Customer> customerList = customerDao.loadAll();
    for (int i = 0; i < customerList.size(); i++) {
      Customer customer = customerList.get(i);
      Card card = new Card.Builder((Context) recycleInputView).setTag(customer.getCooperateId())
          .withProvider(SmallImageCardProvider.class)
          .setTitle(customer.getCooperateName() + "(" + customer.getCooperateId() + ")")
          .setDescription("")
          .endConfig()
          .build();
      recycleInputView.addCard(card);
    }
  }
}
