package com.huaren.logistics.update;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class V1Migration implements Migration{
  @Override public void migrate(SQLiteDatabase db) {
    Log.i(V1Migration.class.getSimpleName(),
            "Upgrade from V1Migration");
  }
}
