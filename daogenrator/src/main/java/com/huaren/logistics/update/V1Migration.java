package com.huaren.logistics.update;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.huaren.logistics.util.Util;

public class V1Migration implements Migration {

    @Override
    public void migrate(SQLiteDatabase db) {
        Log.i(V1Migration.class.getSimpleName(),
                "Upgrade from V1Migration");
        boolean flag = Util.checkColumnExist(db, "RECYCLE_INPUT", "UP_STATUS");
        if(!flag) {
            db.execSQL("ALTER TABLE RECYCLE_INPUT ADD UP_STATUS TEXT");
            db.execSQL("UPDATE RECYCLE_INPUT SET UP_STATUS = '0'");
        }
    }
}
