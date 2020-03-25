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
    public static final String COL_1 ="ID";
    public static final String COL_2="Day";
    public static final String COL_3="Month";
    public static final String COL_4="Event_Name";

    public static final String  CREATE_TABLE="CREATE TABLE "+TABLE_NAME+"("+COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT,"+COL_2+" INT,"+COL_3+" INT,"+COL_4+" TEXT);";

    public MyDB(@Nullable Context context) {
        super(context,"Database",null,3);
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
    public Cursor GetEventOn(int month){
        SQLiteDatabase sr=this.getReadableDatabase();
        return sr.rawQuery("SELECT * FROM "+ TABLE_NAME +" WHERE "+COL_3+" = "+month,null);
    }
    public Cursor getEventofDay(int day,int month){
        SQLiteDatabase sq=this.getReadableDatabase();
        return sq.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+COL_2+" = "+day+" AND "+COL_3+" = "+month,null);
    }
    public void deleteonID(int id){
        SQLiteDatabase sq=this.getReadableDatabase();
        sq.execSQL("DELETE FROM "+TABLE_NAME+" WHERE "+COL_1+" = "+id+";");
    }
    public boolean updateevent(int id, String s) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE "+TABLE_NAME+" SET Event_Name = "+"'"+s+"' "+ "WHERE ID = "+id );
        return true;
    }

}
