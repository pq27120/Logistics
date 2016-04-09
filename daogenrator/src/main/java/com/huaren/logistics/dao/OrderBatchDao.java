package com.huaren.logistics.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.huaren.logistics.bean.OrderBatch;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ORDER_BATCH.
*/
public class OrderBatchDao extends AbstractDao<OrderBatch, String> {

    public static final String TABLENAME = "ORDER_BATCH";

    /**
     * Properties of entity OrderBatch.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
        public final static Property CooperateId = new Property(1, String.class, "cooperateId", false, "COOPERATE_ID");
        public final static Property SPdtgCustfullname = new Property(2, String.class, "sPdtgCustfullname", false, "S_PDTG_CUSTFULLNAME");
        public final static Property LPdtgBatch = new Property(3, int.class, "lPdtgBatch", false, "L_PDTG_BATCH");
        public final static Property DriversID = new Property(4, String.class, "driversID", false, "DRIVERS_ID");
        public final static Property Evaluation = new Property(5, String.class, "evaluation", false, "EVALUATION");
        public final static Property CanEvalutaion = new Property(6, String.class, "canEvalutaion", false, "CAN_EVALUTAION");
        public final static Property Status = new Property(7, String.class, "status", false, "STATUS");
        public final static Property AddTime = new Property(8, java.util.Date.class, "addTime", false, "ADD_TIME");
        public final static Property EditTime = new Property(9, java.util.Date.class, "editTime", false, "EDIT_TIME");
    };


    public OrderBatchDao(DaoConfig config) {
        super(config);
    }
    
    public OrderBatchDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ORDER_BATCH' (" + //
                "'ID' TEXT PRIMARY KEY NOT NULL ," + // 0: id
                "'COOPERATE_ID' TEXT NOT NULL ," + // 1: cooperateId
                "'S_PDTG_CUSTFULLNAME' TEXT," + // 2: sPdtgCustfullname
                "'L_PDTG_BATCH' INTEGER NOT NULL ," + // 3: lPdtgBatch
                "'DRIVERS_ID' TEXT NOT NULL ," + // 4: driversID
                "'EVALUATION' TEXT," + // 5: evaluation
                "'CAN_EVALUTAION' TEXT," + // 6: canEvalutaion
                "'STATUS' TEXT NOT NULL ," + // 7: status
                "'ADD_TIME' INTEGER NOT NULL ," + // 8: addTime
                "'EDIT_TIME' INTEGER);"); // 9: editTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ORDER_BATCH'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, OrderBatch entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
        stmt.bindString(2, entity.getCooperateId());
 
        String sPdtgCustfullname = entity.getSPdtgCustfullname();
        if (sPdtgCustfullname != null) {
            stmt.bindString(3, sPdtgCustfullname);
        }
        stmt.bindLong(4, entity.getLPdtgBatch());
        stmt.bindString(5, entity.getDriversID());
 
        String evaluation = entity.getEvaluation();
        if (evaluation != null) {
            stmt.bindString(6, evaluation);
        }
 
        String canEvalutaion = entity.getCanEvalutaion();
        if (canEvalutaion != null) {
            stmt.bindString(7, canEvalutaion);
        }
        stmt.bindString(8, entity.getStatus());
        stmt.bindLong(9, entity.getAddTime().getTime());
 
        java.util.Date editTime = entity.getEditTime();
        if (editTime != null) {
            stmt.bindLong(10, editTime.getTime());
        }
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public OrderBatch readEntity(Cursor cursor, int offset) {
        OrderBatch entity = new OrderBatch( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.getString(offset + 1), // cooperateId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // sPdtgCustfullname
            cursor.getInt(offset + 3), // lPdtgBatch
            cursor.getString(offset + 4), // driversID
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // evaluation
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // canEvalutaion
            cursor.getString(offset + 7), // status
            new java.util.Date(cursor.getLong(offset + 8)), // addTime
            cursor.isNull(offset + 9) ? null : new java.util.Date(cursor.getLong(offset + 9)) // editTime
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, OrderBatch entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setCooperateId(cursor.getString(offset + 1));
        entity.setSPdtgCustfullname(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setLPdtgBatch(cursor.getInt(offset + 3));
        entity.setDriversID(cursor.getString(offset + 4));
        entity.setEvaluation(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setCanEvalutaion(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setStatus(cursor.getString(offset + 7));
        entity.setAddTime(new java.util.Date(cursor.getLong(offset + 8)));
        entity.setEditTime(cursor.isNull(offset + 9) ? null : new java.util.Date(cursor.getLong(offset + 9)));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(OrderBatch entity, long rowId) {
        return entity.getId();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(OrderBatch entity) {
        if(entity != null) {
            return entity.getId();
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