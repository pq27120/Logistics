package com.huaren.logistics.update;

import android.database.sqlite.SQLiteDatabase;

public interface Migration {
  void migrate(SQLiteDatabase db);
}
