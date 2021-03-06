package com.huaren.logistics.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.huaren.logistics.bean.LogisticsUser;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table LOGISTICS_USER.
*/
public class LogisticsUserDao extends AbstractDao<LogisticsUser, Long> {

    public static final String TABLENAME = "LOGISTICS_USER";

    /**
     * Properties of entity LogisticsUser.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "ID");
        public final static Property UserName = new Property(1, String.class, "userName", false, "USER_NAME");
        public final static Property Pwd = new Property(2, String.class, "pwd", false, "PWD");
        public final static Property DriverId = new Property(3, String.class, "driverId", false, "DRIVER_ID");
    };


    public LogisticsUserDao(DaoConfig config) {
        super(config);
    }
    
    public LogisticsUserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'LOGISTICS_USER' (" + //
                "'ID' INTEGER PRIMARY KEY ," + // 0: id
                "'USER_NAME' TEXT NOT NULL ," + // 1: userName
                "'PWD' TEXT NOT NULL ," + // 2: pwd
                "'DRIVER_ID' TEXT);"); // 3: driverId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'LOGISTICS_USER'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, LogisticsUser entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getUserName());
        stmt.bindString(3, entity.getPwd());
 
        String driverId = entity.getDriverId();
        if (driverId != null) {
            stmt.bindString(4, driverId);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public LogisticsUser readEntity(Cursor cursor, int offset) {
        LogisticsUser entity = new LogisticsUser( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // userName
            cursor.getString(offset + 2), // pwd
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // driverId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, LogisticsUser entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserName(cursor.getString(offset + 1));
        entity.setPwd(cursor.getString(offset + 2));
        entity.setDriverId(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(LogisticsUser entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(LogisticsUser entity) {
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
