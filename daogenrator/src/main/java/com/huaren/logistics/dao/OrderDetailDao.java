package com.huaren.logistics.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.huaren.logistics.bean.OrderDetail;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ORDER_DETAIL.
*/
public class OrderDetailDao extends AbstractDao<OrderDetail, String> {

    public static final String TABLENAME = "ORDER_DETAIL";

    /**
     * Properties of entity OrderDetail.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property DetailId = new Property(0, String.class, "detailId", true, "DETAIL_ID");
        public final static Property CustomerId = new Property(1, String.class, "customerId", false, "CUSTOMER_ID");
        public final static Property CooperateId = new Property(2, String.class, "cooperateId", false, "COOPERATE_ID");
        public final static Property DispatchNumber = new Property(3, String.class, "dispatchNumber", false, "DISPATCH_NUMBER");
        public final static Property DispatchCreatTime = new Property(4, String.class, "dispatchCreatTime", false, "DISPATCH_CREAT_TIME");
        public final static Property DriversID = new Property(5, String.class, "driversID", false, "DRIVERS_ID");
        public final static Property SPdtgEmplname = new Property(6, String.class, "sPdtgEmplname", false, "S_PDTG_EMPLNAME");
        public final static Property SPdtgEmplname2 = new Property(7, String.class, "sPdtgEmplname2", false, "S_PDTG_EMPLNAME2");
        public final static Property SuicherenyuanID = new Property(8, String.class, "suicherenyuanID", false, "SUICHERENYUAN_ID");
        public final static Property SuicherenyuanID2 = new Property(9, String.class, "suicherenyuanID2", false, "SUICHERENYUAN_ID2");
        public final static Property SPdtgEmplname3 = new Property(10, String.class, "sPdtgEmplname3", false, "S_PDTG_EMPLNAME3");
        public final static Property SPdtgVehicleno = new Property(11, String.class, "sPdtgVehicleno", false, "S_PDTG_VEHICLENO");
        public final static Property CountPieces = new Property(12, String.class, "countPieces", false, "COUNT_PIECES");
        public final static Property LPdtgBatch = new Property(13, int.class, "lPdtgBatch", false, "L_PDTG_BATCH");
        public final static Property Ordered = new Property(14, String.class, "ordered", false, "ORDERED");
        public final static Property OrderId = new Property(15, String.class, "orderId", false, "ORDER_ID");
        public final static Property WaveKey = new Property(16, String.class, "waveKey", false, "WAVE_KEY");
        public final static Property Lpn = new Property(17, String.class, "lpn", false, "LPN");
        public final static Property Mtype = new Property(18, String.class, "mtype", false, "MTYPE");
        public final static Property Uom = new Property(19, String.class, "uom", false, "UOM");
        public final static Property DetailStatus = new Property(20, String.class, "detailStatus", false, "DETAIL_STATUS");
        public final static Property Status = new Property(21, String.class, "status", false, "STATUS");
        public final static Property AddTime = new Property(22, java.util.Date.class, "addTime", false, "ADD_TIME");
        public final static Property EditTime = new Property(23, java.util.Date.class, "editTime", false, "EDIT_TIME");
    };


    public OrderDetailDao(DaoConfig config) {
        super(config);
    }
    
    public OrderDetailDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ORDER_DETAIL' (" + //
                "'DETAIL_ID' TEXT PRIMARY KEY NOT NULL ," + // 0: detailId
                "'CUSTOMER_ID' TEXT NOT NULL ," + // 1: customerId
                "'COOPERATE_ID' TEXT NOT NULL ," + // 2: cooperateId
                "'DISPATCH_NUMBER' TEXT NOT NULL ," + // 3: dispatchNumber
                "'DISPATCH_CREAT_TIME' TEXT NOT NULL ," + // 4: dispatchCreatTime
                "'DRIVERS_ID' TEXT NOT NULL ," + // 5: driversID
                "'S_PDTG_EMPLNAME' TEXT NOT NULL ," + // 6: sPdtgEmplname
                "'S_PDTG_EMPLNAME2' TEXT NOT NULL ," + // 7: sPdtgEmplname2
                "'SUICHERENYUAN_ID' TEXT NOT NULL ," + // 8: suicherenyuanID
                "'SUICHERENYUAN_ID2' TEXT NOT NULL ," + // 9: suicherenyuanID2
                "'S_PDTG_EMPLNAME3' TEXT NOT NULL ," + // 10: sPdtgEmplname3
                "'S_PDTG_VEHICLENO' TEXT NOT NULL ," + // 11: sPdtgVehicleno
                "'COUNT_PIECES' TEXT NOT NULL ," + // 12: countPieces
                "'L_PDTG_BATCH' INTEGER NOT NULL ," + // 13: lPdtgBatch
                "'ORDERED' TEXT NOT NULL ," + // 14: ordered
                "'ORDER_ID' TEXT NOT NULL ," + // 15: orderId
                "'WAVE_KEY' TEXT NOT NULL ," + // 16: waveKey
                "'LPN' TEXT NOT NULL ," + // 17: lpn
                "'MTYPE' TEXT NOT NULL ," + // 18: mtype
                "'UOM' TEXT NOT NULL ," + // 19: uom
                "'DETAIL_STATUS' TEXT NOT NULL ," + // 20: detailStatus
                "'STATUS' TEXT NOT NULL ," + // 21: status
                "'ADD_TIME' INTEGER NOT NULL ," + // 22: addTime
                "'EDIT_TIME' INTEGER);"); // 23: editTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ORDER_DETAIL'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, OrderDetail entity) {
        stmt.clearBindings();
 
        String detailId = entity.getDetailId();
        if (detailId != null) {
            stmt.bindString(1, detailId);
        }
        stmt.bindString(2, entity.getCustomerId());
        stmt.bindString(3, entity.getCooperateId());
        stmt.bindString(4, entity.getDispatchNumber());
        stmt.bindString(5, entity.getDispatchCreatTime());
        stmt.bindString(6, entity.getDriversID());
        stmt.bindString(7, entity.getSPdtgEmplname());
        stmt.bindString(8, entity.getSPdtgEmplname2());
        stmt.bindString(9, entity.getSuicherenyuanID());
        stmt.bindString(10, entity.getSuicherenyuanID2());
        stmt.bindString(11, entity.getSPdtgEmplname3());
        stmt.bindString(12, entity.getSPdtgVehicleno());
        stmt.bindString(13, entity.getCountPieces());
        stmt.bindLong(14, entity.getLPdtgBatch());
        stmt.bindString(15, entity.getOrdered());
        stmt.bindString(16, entity.getOrderId());
        stmt.bindString(17, entity.getWaveKey());
        stmt.bindString(18, entity.getLpn());
        stmt.bindString(19, entity.getMtype());
        stmt.bindString(20, entity.getUom());
        stmt.bindString(21, entity.getDetailStatus());
        stmt.bindString(22, entity.getStatus());
        stmt.bindLong(23, entity.getAddTime().getTime());
 
        java.util.Date editTime = entity.getEditTime();
        if (editTime != null) {
            stmt.bindLong(24, editTime.getTime());
        }
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public OrderDetail readEntity(Cursor cursor, int offset) {
        OrderDetail entity = new OrderDetail( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // detailId
            cursor.getString(offset + 1), // customerId
            cursor.getString(offset + 2), // cooperateId
            cursor.getString(offset + 3), // dispatchNumber
            cursor.getString(offset + 4), // dispatchCreatTime
            cursor.getString(offset + 5), // driversID
            cursor.getString(offset + 6), // sPdtgEmplname
            cursor.getString(offset + 7), // sPdtgEmplname2
            cursor.getString(offset + 8), // suicherenyuanID
            cursor.getString(offset + 9), // suicherenyuanID2
            cursor.getString(offset + 10), // sPdtgEmplname3
            cursor.getString(offset + 11), // sPdtgVehicleno
            cursor.getString(offset + 12), // countPieces
            cursor.getInt(offset + 13), // lPdtgBatch
            cursor.getString(offset + 14), // ordered
            cursor.getString(offset + 15), // orderId
            cursor.getString(offset + 16), // waveKey
            cursor.getString(offset + 17), // lpn
            cursor.getString(offset + 18), // mtype
            cursor.getString(offset + 19), // uom
            cursor.getString(offset + 20), // detailStatus
            cursor.getString(offset + 21), // status
            new java.util.Date(cursor.getLong(offset + 22)), // addTime
            cursor.isNull(offset + 23) ? null : new java.util.Date(cursor.getLong(offset + 23)) // editTime
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, OrderDetail entity, int offset) {
        entity.setDetailId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setCustomerId(cursor.getString(offset + 1));
        entity.setCooperateId(cursor.getString(offset + 2));
        entity.setDispatchNumber(cursor.getString(offset + 3));
        entity.setDispatchCreatTime(cursor.getString(offset + 4));
        entity.setDriversID(cursor.getString(offset + 5));
        entity.setSPdtgEmplname(cursor.getString(offset + 6));
        entity.setSPdtgEmplname2(cursor.getString(offset + 7));
        entity.setSuicherenyuanID(cursor.getString(offset + 8));
        entity.setSuicherenyuanID2(cursor.getString(offset + 9));
        entity.setSPdtgEmplname3(cursor.getString(offset + 10));
        entity.setSPdtgVehicleno(cursor.getString(offset + 11));
        entity.setCountPieces(cursor.getString(offset + 12));
        entity.setLPdtgBatch(cursor.getInt(offset + 13));
        entity.setOrdered(cursor.getString(offset + 14));
        entity.setOrderId(cursor.getString(offset + 15));
        entity.setWaveKey(cursor.getString(offset + 16));
        entity.setLpn(cursor.getString(offset + 17));
        entity.setMtype(cursor.getString(offset + 18));
        entity.setUom(cursor.getString(offset + 19));
        entity.setDetailStatus(cursor.getString(offset + 20));
        entity.setStatus(cursor.getString(offset + 21));
        entity.setAddTime(new java.util.Date(cursor.getLong(offset + 22)));
        entity.setEditTime(cursor.isNull(offset + 23) ? null : new java.util.Date(cursor.getLong(offset + 23)));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(OrderDetail entity, long rowId) {
        return entity.getDetailId();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(OrderDetail entity) {
        if(entity != null) {
            return entity.getDetailId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}