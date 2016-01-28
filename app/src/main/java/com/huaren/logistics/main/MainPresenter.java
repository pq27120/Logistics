package com.huaren.logistics.main;

import android.content.Context;
import com.huaren.logistics.entity.MainMenuItem;
import java.util.ArrayList;
import java.util.List;

public class MainPresenter implements OnFinishedListener {

  private MainView mainView;
  private FindItemsInteractor findItemsInteractor;
  List<MainMenuItem> mainMenuItemList = new ArrayList<>();

  public MainPresenter(MainView mainView) {
    this.mainView = mainView;
    findItemsInteractor = new FindItemsInteractorImpl();
  }

  public void onResume() {
    if (mainView != null) {
      mainView.showProgress();
    }

    findItemsInteractor.findItems(this);
  }

  public void onItemClicked(int position) {
    if (mainView != null) {
      mainView.showMessage(String.format("Position %d clicked", position + 1));
    }
  }

  public void onDestroy() {
    mainView = null;
  }

  @Override public void onFinished(List<String> items) {
    if (mainView != null) {
      mainView.setItems(items);
      mainView.hideProgress();
    }
  }

  public void initGridView() {
    MainMenuItem mainMenuItem = new MainMenuItem();
    mainMenuItem.setId(1);
    mainMenuItem.setImageId("shujuhuoqu");
    mainMenuItem.setName("数据获取");
    mainMenuItemList.add(mainMenuItem);

    mainMenuItem = new MainMenuItem();
    mainMenuItem.setId(2);
    mainMenuItem.setImageId("zhuangchedianhuo");
    mainMenuItem.setName("装车点货");
    mainMenuItemList.add(mainMenuItem);

    mainMenuItem = new MainMenuItem();
    mainMenuItem.setId(3);
    mainMenuItem.setImageId("xiechedianhuo");
    mainMenuItem.setName("卸车点货");
    mainMenuItemList.add(mainMenuItem);

    mainMenuItem = new MainMenuItem();
    mainMenuItem.setId(4);
    mainMenuItem.setImageId("huishouluru");
    mainMenuItem.setName("回收录入");
    mainMenuItemList.add(mainMenuItem);

    mainMenuItem = new MainMenuItem();
    mainMenuItem.setId(5);
    mainMenuItem.setImageId("kehupingjia");
    mainMenuItem.setName("客户评价");
    mainMenuItemList.add(mainMenuItem);

    mainMenuItem = new MainMenuItem();
    mainMenuItem.setId(6);
    mainMenuItem.setImageId("huishousaomiao");
    mainMenuItem.setName("回收扫描");
    mainMenuItemList.add(mainMenuItem);

    mainMenuItem = new MainMenuItem();
    mainMenuItem.setId(7);
    mainMenuItem.setImageId("shangchuan");
    mainMenuItem.setName("上传");
    mainMenuItemList.add(mainMenuItem);

    mainMenuItem = new MainMenuItem();
    mainMenuItem.setId(8);
    mainMenuItem.setImageId("mypage");
    mainMenuItem.setName("我的");
    mainMenuItemList.add(mainMenuItem);

    MainGridViewAdapter adapter = new MainGridViewAdapter((Context) mainView, mainMenuItemList);
    mainView.setGridViewAdapter(adapter);
  }

  public void mainMenuItemClick(int position) {
    MainMenuItem mainMenuItem = mainMenuItemList.get(position);
    int id = mainMenuItem.getId();
    switch (id) {
      case 1:
        mainView.enterDownCargo();
        break;
      case 2:
        mainView.enterCargo();
        break;
      case 3:
        mainView.enterUnCargo();
        break;
      case 4:
        break;
      case 5:
        mainView.enterEvaluaton();
        break;
      case 6:
        break;
      case 7:
        break;
      case 8:
        mainView.enterInfo();
        break;
      default:
        break;
    }
  }
}
