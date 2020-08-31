package com.example.persasist;


import android.provider.BaseColumns;

public class InputContract {
    public static final String DB_NAME = "com.nudger.db";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "remindersAutoSend";

        public static final String REM_VALUE = "value";
        public static final String MOBILE_NO = "mobile";
        public static final String REM_TIME = "time";
    }
}