package com.huaren.logistics.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.huaren.logistics.bean.LogisticsUser;
import com.huaren.logistics.bean.SysDic;
import com.huaren.logistics.bean.SysDicValue;
import com.huaren.logistics.bean.Customer;
import com.huaren.logistics.bean.DownBatchInfo;
import com.huaren.logistics.bean.OrderBatch;
import com.huaren.logistics.bean.OrderDetail;
import com.huaren.logistics.bean.RecycleInput;
import com.huaren.logistics.bean.RecycleScan;
import com.huaren.logistics.bean.OperatorLog;

import com.huaren.logistics.dao.LogisticsUserDao;
import com.huaren.logistics.dao.SysDicDao;
import com.huaren.logistics.dao.SysDicValueDao;
import com.huaren.logistics.dao.CustomerDao;
import com.huaren.logistics.dao.DownBatchInfoDao;
import com.huaren.logistics.dao.OrderBatchDao;
import com.huaren.logistics.dao.OrderDetailDao;
import com.huaren.logistics.dao.RecycleInputDao;
import com.huaren.logistics.dao.RecycleScanDao;
import com.huaren.logistics.dao.OperatorLogDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig logisticsUserDaoConfig;
    private final DaoConfig sysDicDaoConfig;
    private final DaoConfig sysDicValueDaoConfig;
    private final DaoConfig customerDaoConfig;
    private final DaoConfig downBatchInfoDaoConfig;
    private final DaoConfig orderBatchDaoConfig;
    private final DaoConfig orderDetailDaoConfig;
    private final DaoConfig recycleInputDaoConfig;
    private final DaoConfig recycleScanDaoConfig;
    private final DaoConfig operatorLogDaoConfig;

    private final LogisticsUserDao logisticsUserDao;
    private final SysDicDao sysDicDao;
    private final SysDicValueDao sysDicValueDao;
    private final CustomerDao customerDao;
    private final DownBatchInfoDao downBatchInfoDao;
    private final OrderBatchDao orderBatchDao;
    private final OrderDetailDao orderDetailDao;
    private final RecycleInputDao recycleInputDao;
    private final RecycleScanDao recycleScanDao;
    private final OperatorLogDao operatorLogDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        logisticsUserDaoConfig = daoConfigMap.get(LogisticsUserDao.class).clone();
        logisticsUserDaoConfig.initIdentityScope(type);

        sysDicDaoConfig = daoConfigMap.get(SysDicDao.class).clone();
        sysDicDaoConfig.initIdentityScope(type);

        sysDicValueDaoConfig = daoConfigMap.get(SysDicValueDao.class).clone();
        sysDicValueDaoConfig.initIdentityScope(type);

        customerDaoConfig = daoConfigMap.get(CustomerDao.class).clone();
        customerDaoConfig.initIdentityScope(type);

        downBatchInfoDaoConfig = daoConfigMap.get(DownBatchInfoDao.class).clone();
        downBatchInfoDaoConfig.initIdentityScope(type);

        orderBatchDaoConfig = daoConfigMap.get(OrderBatchDao.class).clone();
        orderBatchDaoConfig.initIdentityScope(type);

        orderDetailDaoConfig = daoConfigMap.get(OrderDetailDao.class).clone();
        orderDetailDaoConfig.initIdentityScope(type);

        recycleInputDaoConfig = daoConfigMap.get(RecycleInputDao.class).clone();
        recycleInputDaoConfig.initIdentityScope(type);

        recycleScanDaoConfig = daoConfigMap.get(RecycleScanDao.class).clone();
        recycleScanDaoConfig.initIdentityScope(type);

        operatorLogDaoConfig = daoConfigMap.get(OperatorLogDao.class).clone();
        operatorLogDaoConfig.initIdentityScope(type);

        logisticsUserDao = new LogisticsUserDao(logisticsUserDaoConfig, this);
        sysDicDao = new SysDicDao(sysDicDaoConfig, this);
        sysDicValueDao = new SysDicValueDao(sysDicValueDaoConfig, this);
        customerDao = new CustomerDao(customerDaoConfig, this);
        downBatchInfoDao = new DownBatchInfoDao(downBatchInfoDaoConfig, this);
        orderBatchDao = new OrderBatchDao(orderBatchDaoConfig, this);
        orderDetailDao = new OrderDetailDao(orderDetailDaoConfig, this);
        recycleInputDao = new RecycleInputDao(recycleInputDaoConfig, this);
        recycleScanDao = new RecycleScanDao(recycleScanDaoConfig, this);
        operatorLogDao = new OperatorLogDao(operatorLogDaoConfig, this);

        registerDao(LogisticsUser.class, logisticsUserDao);
        registerDao(SysDic.class, sysDicDao);
        registerDao(SysDicValue.class, sysDicValueDao);
        registerDao(Customer.class, customerDao);
        registerDao(DownBatchInfo.class, downBatchInfoDao);
        registerDao(OrderBatch.class, orderBatchDao);
        registerDao(OrderDetail.class, orderDetailDao);
        registerDao(RecycleInput.class, recycleInputDao);
        registerDao(RecycleScan.class, recycleScanDao);
        registerDao(OperatorLog.class, operatorLogDao);
    }
    
    public void clear() {
        logisticsUserDaoConfig.getIdentityScope().clear();
        sysDicDaoConfig.getIdentityScope().clear();
        sysDicValueDaoConfig.getIdentityScope().clear();
        customerDaoConfig.getIdentityScope().clear();
        downBatchInfoDaoConfig.getIdentityScope().clear();
        orderBatchDaoConfig.getIdentityScope().clear();
        orderDetailDaoConfig.getIdentityScope().clear();
        recycleInputDaoConfig.getIdentityScope().clear();
        recycleScanDaoConfig.getIdentityScope().clear();
        operatorLogDaoConfig.getIdentityScope().clear();
    }

    public LogisticsUserDao getLogisticsUserDao() {
        return logisticsUserDao;
    }

    public SysDicDao getSysDicDao() {
        return sysDicDao;
    }

    public SysDicValueDao getSysDicValueDao() {
        return sysDicValueDao;
    }

    public CustomerDao getCustomerDao() {
        return customerDao;
    }

    public DownBatchInfoDao getDownBatchInfoDao() {
        return downBatchInfoDao;
    }

    public OrderBatchDao getOrderBatchDao() {
        return orderBatchDao;
    }

    public OrderDetailDao getOrderDetailDao() {
        return orderDetailDao;
    }

    public RecycleInputDao getRecycleInputDao() {
        return recycleInputDao;
    }

    public RecycleScanDao getRecycleScanDao() {
        return recycleScanDao;
    }

    public OperatorLogDao getOperatorLogDao() {
        return operatorLogDao;
    }

}
