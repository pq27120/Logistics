package com.huaren.logistics.update;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class V1Migration implements Migration {

    @Override
    public void migrate(SQLiteDatabase db) {
        Log.i(V1Migration.class.getSimpleName(),
                "Upgrade from V1Migration");
        db.execSQL("ALTER TABLE RECYCLE_INPUT ADD UP_STATUS TEXT");
        db.execSQL("UPDATE RECYCLE_INPUT SET UP_STATUS = '0'");
    }
}
