package com.example.uetplus2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBDesigner extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "uetplus.db";
    private static final int 	DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE_TABLE_TIMETABLES = "create table timetables "
            + "(id integer primary key autoincrement,"
            + "subject_name text not null,"
            + "credit text not null,"
            + "subject_code text not null,"
            + "teacher text not null,"
            + "student_total text not null,"
            + "day_time text not null,"
            + "day_week text not null,"
            + "lesson text not null,"
            + "room text not null,"
            + "note text not null);";

    public DBDesigner(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_TABLE_TIMETABLES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBDesigner.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS timetables");
        onCreate(db);
    }
}