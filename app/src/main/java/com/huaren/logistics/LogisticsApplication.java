package com.huaren.logistics;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.device.ScanManager;

import com.huaren.logistics.dao.CustomerDao;
import com.huaren.logistics.dao.DaoMaster;
import com.huaren.logistics.dao.DaoSession;
import com.huaren.logistics.dao.DownBatchInfoDao;
import com.huaren.logistics.dao.ErrOperatorLogDao;
import com.huaren.logistics.dao.LogisticsUserDao;
import com.huaren.logistics.dao.OperatorLogDao;
import com.huaren.logistics.dao.OrderBatchDao;
import com.huaren.logistics.dao.OrderDetailDao;
import com.huaren.logistics.dao.RecycleInputDao;
import com.huaren.logistics.dao.RecycleScanDao;
import com.huaren.logistics.dao.SysDicDao;
import com.huaren.logistics.dao.SysDicValueDao;
import com.huaren.logistics.update.SQLiteOpenHelper;
import com.huaren.logistics.util.CommonTool;
import com.huaren.logistics.util.SoundPoolUtil;

public class LogisticsApplication extends Application {

  private SQLiteDatabase db;
  private DaoMaster daoMaster;
  private DaoSession daoSession;
  private CustomerDao customerDao;
  private OrderDetailDao orderDetailDao;
  private SysDicDao sysDicDao;
  private SysDicValueDao sysDicValueDao;
  private RecycleInputDao recycleInputDao;
  private RecycleScanDao recycleScanDao;
  private LogisticsUserDao logisticsUserDao;
  private DownBatchInfoDao downBatchInfoDao;
  private OrderBatchDao orderBatchDao;
  private OperatorLogDao operatorLogDao;
  private ErrOperatorLogDao errOperatorLogDao;
  private static LogisticsApplication INSTANCE;
  private static Context context;
  private SoundPoolUtil soundPoolUtil;

  public static LogisticsApplication getInstance() {
    return INSTANCE;
  }

  @Override public void onCreate() {
    super.onCreate();
    INSTANCE = this;
    context = getApplicationContext();
    CrashHandler crashHandler = CrashHandler.getInstance();
    crashHandler.init(getApplicationContext());
    setupDatabase();
    soundPoolUtil = new SoundPoolUtil(context);

    try {
      ScanManager scan = new ScanManager();
      scan.openScanner();
      scan.switchOutputMode(1);
    } catch (Exception e) {
      e.printStackTrace();
      CommonTool.showLog("初始化扫描设备异常！");
    }
  }

  /**
   * 初始化数据库链接
   */
  private void setupDatabase() {
    // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
    // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
    // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
    // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
//    DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "logistics-db", null);
    DaoMaster.OpenHelper helper = new SQLiteOpenHelper(this, null);
    db = helper.getWritableDatabase();
    // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
    daoMaster = new DaoMaster(db);
    daoSession = daoMaster.newSession();
    initDao();
  }

  private void initDao() {
    customerDao = daoSession.getCustomerDao();
    orderDetailDao = daoSession.getOrderDetailDao();
    sysDicDao = daoSession.getSysDicDao();
    sysDicValueDao = daoSession.getSysDicValueDao();
    recycleInputDao = daoSession.getRecycleInputDao();
    recycleScanDao = daoSession.getRecycleScanDao();
    logisticsUserDao = daoSession.getLogisticsUserDao();
    downBatchInfoDao = daoSession.getDownBatchInfoDao();
    orderBatchDao = daoSession.getOrderBatchDao();
    operatorLogDao = daoSession.getOperatorLogDao();
    errOperatorLogDao = daoSession.getErrOperatorLogDao();
  }

  public DaoSession getDaoSession() {
    return daoSession;
  }

  public OrderDetailDao getOrderDetailDao() {
    return orderDetailDao;
  }

  public SysDicDao getSysDicDao() {
    return sysDicDao;
  }

  public SysDicValueDao getSysDicValueDao() {
    return sysDicValueDao;
  }

  public RecycleInputDao getRecycleInputDao() {
    return recycleInputDao;
  }

  public RecycleScanDao getRecycleScanDao() {
    return recycleScanDao;
  }

  public CustomerDao getCustomerDao() {
    return customerDao;
  }

  public static Context getContext() {
    return context;
  }

  public LogisticsUserDao getLogisticsUserDao() {
    return logisticsUserDao;
  }

  public DownBatchInfoDao getDownBatchInfoDao() {
    return downBatchInfoDao;
  }

  public OrderBatchDao getOrderBatchDao() {
    return orderBatchDao;
  }

  public SoundPoolUtil getSoundPoolUtil() {
    return soundPoolUtil;
  }

  public OperatorLogDao getOperatorLogDao() {
    return operatorLogDao;
  }

  public ErrOperatorLogDao getErrOperatorLogDao() {
    return errOperatorLogDao;
  }
}
