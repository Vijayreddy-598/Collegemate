package com.batch16.collegemate.Functions;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDB extends SQLiteOpenHelper {

    // Database Name
    private static final String DATABASE_NAME = "Database";
    //Table Name
    public static final String Event_Table ="Events";
    public static final String Attendance_Table="Attendance";
    //Event table Column Names
    public static final String E_COL_1 ="ID";
    public static final String E_COL_2="Day";
    public static final String E_COL_3 ="Month";
    public static final String E_COL_4 ="Event_Name";
    //Attendance table Column Names
    public static final String A_COL_1 ="ID";
    public static final String A_COL_2="Day";
    public static final String A_COL_3 ="Month";
    public static final String A_COL_4 ="Event";


    //Event table Create Statements
    public static final String  CREATE_Event_TABLE="CREATE TABLE "+ Event_Table +"("+E_COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT,"+E_COL_2+" INT,"+ E_COL_3 +" INT,"+ E_COL_4 +" TEXT);";

    //Event table Create Statements
    public static final String  CREATE_Attendance_TABLE="CREATE TABLE "+ Attendance_Table +"("+A_COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT,"+A_COL_2+" INT,"+ A_COL_3 +" INT,"+ A_COL_4 +" TEXT);";

    public MyDB(@Nullable Context context) {
        super(context,DATABASE_NAME,null,3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_Event_TABLE);
        db.execSQL(CREATE_Attendance_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF  EXISTS "+ Event_Table);
        db.execSQL("DROP TABLE IF  EXISTS "+ Attendance_Table);
        // create new tables
        onCreate(db);
    }
    public void insertData(ContentValues cv) {
        SQLiteDatabase sq=this.getWritableDatabase();
        sq.insert(Event_Table,null,cv);
    }
    public Cursor readEventdata(){
        SQLiteDatabase sr=this.getReadableDatabase();
        return sr.rawQuery("SELECT * FROM "+ Event_Table,null);
    }
    public Cursor getEventofDay(int day,int month){
        SQLiteDatabase sq=this.getReadableDatabase();
        return sq.rawQuery("SELECT * FROM "+ Event_Table +" WHERE "+E_COL_2+" = "+day+" AND "+ E_COL_3 +" = "+month,null);
    }
    public void deleteonID(int id){
        SQLiteDatabase sq=this.getReadableDatabase();
        sq.execSQL("DELETE FROM "+ Event_Table +" WHERE "+E_COL_1+" = "+id+";");
    }
    public boolean updateevent(int id, String s) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE "+ Event_Table +" SET Event_Name = "+"'"+s+"' "+ "WHERE ID = "+id );
        return true;
    }
    public void insertAttendance(ContentValues cv) {
        SQLiteDatabase sq=this.getWritableDatabase();
        sq.insert(Event_Table,null,cv);
    }
    public Cursor readAttendance(){
        SQLiteDatabase sr=this.getReadableDatabase();
        return sr.rawQuery("SELECT * FROM "+ Attendance_Table,null);
    }

    /*
    public Cursor GetEventOn(int month){
        SQLiteDatabase sr=this.getReadableDatabase();
        return sr.rawQuery("SELECT * FROM "+ Event_Table +" WHERE "+ E_COL_3 +" = "+month,null);
    }
    public long getProfilesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, Event_Table);
        db.close();
        return count;
    }*/
}
