package com.batch16.collegemate.Functions;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDB extends SQLiteOpenHelper {
    public static final String TABLE_NAME="Attendance";
    public static final String COL_1="Name";
    public static final String COL_2="Day";
    public static final String COL_3="Month";
    public static final String COL_4="Attendance";
    public static final String  CREATE_TABLE="CREATE TABLE "+TABLE_NAME+"("+COL_1+" TEXT,"+COL_2+" INT,"+COL_3+" INT,"+COL_4+" INT);";

    public MyDB(@Nullable Context context) {
        super(context,"Database",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF  EXISTS "+TABLE_NAME);
        onCreate(db);
    }
    public void insertData(ContentValues cv) {
        SQLiteDatabase sq=this.getWritableDatabase();
        sq.insert(TABLE_NAME,null,cv);
    }
    public Cursor readdata(){
        SQLiteDatabase sr=this.getReadableDatabase();
        return sr.rawQuery("SELECT * FROM "+TABLE_NAME,null);
    }

}
