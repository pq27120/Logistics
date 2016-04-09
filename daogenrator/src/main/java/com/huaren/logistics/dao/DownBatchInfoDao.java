package com.huaren.logistics.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.huaren.logistics.bean.DownBatchInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table DOWN_BATCH_INFO.
*/
public class DownBatchInfoDao extends AbstractDao<DownBatchInfo, Long> {

    public static final String TABLENAME = "DOWN_BATCH_INFO";

    /**
     * Properties of entity DownBatchInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property LPdtgBatch = new Property(0, Long.class, "lPdtgBatch", true, "L_PDTG_BATCH");
        public final static Property AddTime = new Property(1, java.util.Date.class, "addTime", false, "ADD_TIME");
    };


    public DownBatchInfoDao(DaoConfig config) {
        super(config);
    }
    
    public DownBatchInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'DOWN_BATCH_INFO' (" + //
                "'L_PDTG_BATCH' INTEGER PRIMARY KEY ," + // 0: lPdtgBatch
                "'ADD_TIME' INTEGER NOT NULL );"); // 1: addTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'DOWN_BATCH_INFO'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, DownBatchInfo entity) {
        stmt.clearBindings();
 
        Long lPdtgBatch = entity.getLPdtgBatch();
        if (lPdtgBatch != null) {
            stmt.bindLong(1, lPdtgBatch);
        }
        stmt.bindLong(2, entity.getAddTime().getTime());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public DownBatchInfo readEntity(Cursor cursor, int offset) {
        DownBatchInfo entity = new DownBatchInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // lPdtgBatch
            new java.util.Date(cursor.getLong(offset + 1)) // addTime
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, DownBatchInfo entity, int offset) {
        entity.setLPdtgBatch(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setAddTime(new java.util.Date(cursor.getLong(offset + 1)));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(DownBatchInfo entity, long rowId) {
        entity.setLPdtgBatch(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(DownBatchInfo entity) {
        if(entity != null) {
            return entity.getLPdtgBatch();
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
