package com.example.fileshare;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProjectDB extends SQLiteOpenHelper {
//    Database MetaData
    private static final String DATABASE_NAME = "projectdb";
    private static final int DATABASE_VERSION = 1;

    //Constructor
    public ProjectDB(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    //onCreate -> methods for database operations
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO: Define table creation logic
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: Define database upgrade logic
    }
}
