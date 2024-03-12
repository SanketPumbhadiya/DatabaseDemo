package com.example.database_demo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SqlDb extends SQLiteOpenHelper {

    private static final String Tablename = "listTable";
    private static final String Firstname = "firstname";
    private static final String Lastname = "lastname";

    public SqlDb(@Nullable Context context) {
        super(context, "listDb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String s = "CREATE TABLE " + Tablename + "("
                + Firstname + " Text, "
                + Lastname + " Text)";
        db.execSQL(s);
    }

    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tablename);
        onCreate(db);
    }

    public void Insertdata(String firstname, String lastname) {
        SQLiteDatabase sdb = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("firstname", firstname);
        cv.put("lastname", lastname);
        sdb.insert(Tablename, null, cv);
    }

    public ArrayList<String> Displaydata() {
        ArrayList<String> data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {Firstname, Lastname};
        Cursor cursor = db.query(Tablename, columns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String fn = cursor.getString(cursor.getColumnIndex(Firstname));
            String ln = cursor.getString(cursor.getColumnIndex(Lastname));
            data.add(fn + " " + ln);
        }
        cursor.close();
        db.close();

        return data;
    }

    public void Deletedata(String firstname) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Tablename,"firstname = ?",new String[]{firstname});
        db.close();
    }

    public void UpdateData(String firstname, String lastname) {
        SQLiteDatabase sdb = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("firstname", firstname);
        cv.put("lastname", lastname);
        sdb.update(Tablename, cv, "firstname = ?", new String[]{firstname});
    }
}
