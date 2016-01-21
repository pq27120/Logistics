package com.huaren.logistics;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import com.huaren.logistics.dao.DaoMaster;
import com.huaren.logistics.dao.DaoSession;

public class LogisticsApplication extends Application {

  private SQLiteDatabase db;
  private DaoMaster daoMaster;
  private DaoSession daoSession;
  private static LogisticsApplication INSTANCE;

  public static LogisticsApplication getInstance() {
    return INSTANCE;
  }

  @Override public void onCreate() {
    super.onCreate();
    INSTANCE = this;
    setupDatabase();
  }

  /**
   * 初始化数据库链接
   */
  private void setupDatabase() {
    // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
    // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
    // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
    // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
    DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "logistics-db", null);
    db = helper.getWritableDatabase();
    // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
    daoMaster = new DaoMaster(db);
    daoSession = daoMaster.newSession();
  }

  public DaoSession getDaoSession() {
    return daoSession;
  }
}
