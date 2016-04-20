package com.huaren.logistics.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.huaren.logistics.bean.Customer;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table CUSTOMER.
*/
public class CustomerDao extends AbstractDao<Customer, String> {

    public static final String TABLENAME = "CUSTOMER";

    /**
     * Properties of entity Customer.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
        public final static Property CooperateId = new Property(1, String.class, "cooperateId", false, "COOPERATE_ID");
        public final static Property LPdtgBatch = new Property(2, int.class, "lPdtgBatch", false, "L_PDTG_BATCH");
        public final static Property SPdtgCustfullname = new Property(3, String.class, "sPdtgCustfullname", false, "S_PDTG_CUSTFULLNAME");
        public final static Property CoopPwd = new Property(4, String.class, "coopPwd", false, "COOP_PWD");
        public final static Property Status = new Property(5, String.class, "status", false, "STATUS");
        public final static Property AddTime = new Property(6, java.util.Date.class, "addTime", false, "ADD_TIME");
        public final static Property EditTime = new Property(7, java.util.Date.class, "editTime", false, "EDIT_TIME");
        public final static Property UserName = new Property(8, String.class, "userName", false, "USER_NAME");
    };


    public CustomerDao(DaoConfig config) {
        super(config);
    }
    
    public CustomerDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'CUSTOMER' (" + //
                "'ID' TEXT PRIMARY KEY NOT NULL ," + // 0: id
                "'COOPERATE_ID' TEXT NOT NULL ," + // 1: cooperateId
                "'L_PDTG_BATCH' INTEGER NOT NULL ," + // 2: lPdtgBatch
                "'S_PDTG_CUSTFULLNAME' TEXT NOT NULL ," + // 3: sPdtgCustfullname
                "'COOP_PWD' TEXT NOT NULL ," + // 4: coopPwd
                "'STATUS' TEXT NOT NULL ," + // 5: status
                "'ADD_TIME' INTEGER NOT NULL ," + // 6: addTime
                "'EDIT_TIME' INTEGER," + // 7: editTime
                "'USER_NAME' TEXT NOT NULL );"); // 8: userName
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'CUSTOMER'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Customer entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
        stmt.bindString(2, entity.getCooperateId());
        stmt.bindLong(3, entity.getLPdtgBatch());
        stmt.bindString(4, entity.getSPdtgCustfullname());
        stmt.bindString(5, entity.getCoopPwd());
        stmt.bindString(6, entity.getStatus());
        stmt.bindLong(7, entity.getAddTime().getTime());
 
        java.util.Date editTime = entity.getEditTime();
        if (editTime != null) {
            stmt.bindLong(8, editTime.getTime());
        }
        stmt.bindString(9, entity.getUserName());
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Customer readEntity(Cursor cursor, int offset) {
        Customer entity = new Customer( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.getString(offset + 1), // cooperateId
            cursor.getInt(offset + 2), // lPdtgBatch
            cursor.getString(offset + 3), // sPdtgCustfullname
            cursor.getString(offset + 4), // coopPwd
            cursor.getString(offset + 5), // status
            new java.util.Date(cursor.getLong(offset + 6)), // addTime
            cursor.isNull(offset + 7) ? null : new java.util.Date(cursor.getLong(offset + 7)), // editTime
            cursor.getString(offset + 8) // userName
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Customer entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setCooperateId(cursor.getString(offset + 1));
        entity.setLPdtgBatch(cursor.getInt(offset + 2));
        entity.setSPdtgCustfullname(cursor.getString(offset + 3));
        entity.setCoopPwd(cursor.getString(offset + 4));
        entity.setStatus(cursor.getString(offset + 5));
        entity.setAddTime(new java.util.Date(cursor.getLong(offset + 6)));
        entity.setEditTime(cursor.isNull(offset + 7) ? null : new java.util.Date(cursor.getLong(offset + 7)));
        entity.setUserName(cursor.getString(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(Customer entity, long rowId) {
        return entity.getId();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(Customer entity) {
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
