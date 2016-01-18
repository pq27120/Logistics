package com.huaren.logistics.downcargo;

import android.content.Context;
import com.huaren.logistics.entity.Customer;
import com.huaren.logistics.entity.Goods;
import com.huaren.logistics.splash.ISplashView;
import java.util.ArrayList;
import java.util.List;

public class DownCargoPresenter {

  private IDownCargoView downCargoView;

  private List<Customer> customerList;

  public DownCargoPresenter(IDownCargoView downCargoView) {
    this.downCargoView = downCargoView;
  }

  public void initList() {
    List<Customer> customerList = new ArrayList<>();
    Customer customer = new Customer();
    customer.setName("客户名称1");
    customer.setAddress("地址名称1");
    customer.setCode("code1");

    List<Goods> goodsList = new ArrayList<>();
    Goods goods = new Goods();
    goods.setName("货物1");
    goods.setId("001");
    goods.setBarcode("bar001");
    goodsList.add(goods);

    goods = new Goods();
    goods.setName("货物2");
    goods.setId("002");
    goods.setBarcode("bar002");
    goodsList.add(goods);

    goods = new Goods();
    goods.setName("货物3");
    goods.setId("003");
    goods.setBarcode("bar003");
    goodsList.add(goods);
    customer.setUnloadedGoodsList(goodsList);
    customerList.add(customer);

    customer = new Customer();
    customer.setName("客户名称2");
    customer.setAddress("地址名称2");
    customer.setCode("code2");

    goodsList = new ArrayList<>();
    goods = new Goods();
    goods.setName("货物11");
    goods.setId("0011");
    goods.setBarcode("bar0011");
    goodsList.add(goods);

    goods = new Goods();
    goods.setName("货物21");
    goods.setId("0021");
    goods.setBarcode("bar0021");
    goodsList.add(goods);

    goods = new Goods();
    goods.setName("货物31");
    goods.setId("0031");
    goods.setBarcode("bar0031");
    goodsList.add(goods);
    customer.setUnloadedGoodsList(goodsList);
    customerList.add(customer);
    DownCargoAdapter adapter = new DownCargoAdapter((Context) downCargoView, customerList);
    downCargoView.setAdapter(adapter);
  }
}
