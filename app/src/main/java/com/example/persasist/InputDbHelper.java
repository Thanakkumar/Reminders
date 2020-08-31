package com.example.persasist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InputDbHelper extends SQLiteOpenHelper {

    public InputDbHelper(Context context) {
        super(context, InputContract.DB_NAME, null, InputContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + InputContract.TaskEntry.TABLE + "    ( " +
                InputContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                InputContract.TaskEntry.REM_VALUE + " TEXT NOT NULL, "+
                InputContract.TaskEntry.REM_TIME +" TEXT NOT NULL, "+InputContract.TaskEntry.MOBILE_NO +" TEXT NOT NULL);";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + InputContract.TaskEntry.TABLE);
        onCreate(db);
    }
}
