package com.huaren.logistics.downcargo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.widget.ListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huaren.logistics.R;
import com.huaren.logistics.entity.Customer;
import com.huaren.logistics.entity.Goods;
import com.huaren.logistics.splash.ISplashView;
import com.huaren.logistics.util.CommonTool;
import com.huaren.logistics.util.http.NetParam;
import java.util.ArrayList;
import java.util.List;

public class DownCargoPresenter {

  private static final int REFRESH = 1;

  private IDownCargoView downCargoView;

  private List<Customer> customerList;

  private DownCargoAdapter adapter;
  /**
   * 当前页数
   */
  int currPage = 1;
  /**
   * 总页数
   */
  int totalPage = 2;
  /**
   * 每页显示多少条
   */
  String length = "8";

  public DownCargoPresenter(IDownCargoView downCargoView) {
    this.downCargoView = downCargoView;
  }

  public void initList(boolean refresh) {
    if (CommonTool.isWifiConnected((Context) downCargoView)) {
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
      downCargoView.showUpdateView();
      if (customerList != null && !customerList.isEmpty()) {
        if (refresh) {
          if (downCargoView.isFooterShown() && currPage < totalPage) {
            currPage++;
            adapter.addInfo(customerList);
          }
          adapter.notifyDataSetChanged();
          mHandler.sendEmptyMessageDelayed(REFRESH, 20);
        } else {
          adapter = new DownCargoAdapter((Context) downCargoView, customerList);
          downCargoView.initPullToRefreshListView(adapter);
        }
      } else {
        adapter = new DownCargoAdapter((Context) downCargoView, new ArrayList<Customer>());
        adapter.notifyDataSetChanged();
        mHandler.sendEmptyMessageDelayed(REFRESH, 20);
      }

      if (!refresh) {// 第一次加载
        //if (result.getPageInfo() != null) {
        //  int totalSize = Integer.parseInt(result.getPageInfo().getTotalSize());
        //  int lenth = Integer.parseInt(result.getPageInfo().getLength());
        //  totalPage = totalSize % lenth == 0 ? totalSize / lenth : totalSize / lenth + 1;
        //}
      }
    } else {
      downCargoView.hideUpdateView();
    }
  }

  Handler mHandler = new Handler(){
      public void handleMessage(Message msg) {
        try {
          switch (msg.what) {
            case 1:
              downCargoView.onRefreshComplete();
              break;
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
  };
}
