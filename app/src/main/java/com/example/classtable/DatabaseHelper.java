package com.example.classtable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table courses(" +
                "id integer primary key autoincrement," +
                "courseName text," +
                "teacher text," +
                "classRoom text," +
                "day integer," +
                "classStart integer," +
                "classEnd integer,"+
                "weekStart integer,"+
                "weekEnd integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
