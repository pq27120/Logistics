package com.huaren.logistics.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by bj on 2016/9/3.
 */
public class Util {
    /**
     * 方法1：检查某表列是否存在
     * @param db
     * @param tableName 表名
     * @param columnName 列名
     * @return
     */
    public static boolean checkColumnExist(SQLiteDatabase db, String tableName
            , String columnName) {
        boolean result = false ;
        Cursor cursor = null ;
        try{
            //查询一行
            cursor = db.rawQuery( "SELECT * FROM " + tableName + " LIMIT 0"
                    , null );
            result = cursor != null && cursor.getColumnIndex(columnName) != -1 ;
        }catch (Exception e){
        }finally{
            if(null != cursor && !cursor.isClosed()){
                cursor.close() ;
            }
        }
        return result ;
    }
}
