package com.example.database_demo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SqlDb extends SQLiteOpenHelper {

    private static final String Tablename = "listTable";
    private static final int Database_version_For_Add_Column = 3;
    private static final int Database_version_For_Add_Gender_Column = 4;
    private static final String Firstname = "firstname";
    private static final String Lastname = "lastname";
    private static final String Gender = "gender";

    private static final int DataBase_Version = Database_version_For_Add_Gender_Column;

    public SqlDb(@Nullable Context context) {
        super(context, "listDb", null, DataBase_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        CreateTableForAddColumn(db);
    }

    public void CreateTableForAddColumn(SQLiteDatabase db) {
        String s = "CREATE TABLE " + Tablename + "("
                + Firstname + " Text, "
                + Lastname + " Text,"
                + Gender + " Text)";
        db.execSQL(s);
    }

    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("Hello", "upgrade function call..");
        if (newVersion > oldVersion && oldVersion < Database_version_For_Add_Column) {
            db.execSQL("ALTER Table " + Tablename + " ADD COLUMN " + Gender + " Text ");
            Log.w("Version", "Table New column add..");
        }
    }

    public void Insertdata(String firstname, String lastname, String gender) {
        SQLiteDatabase sdb = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("firstname", firstname);
        cv.put("lastname", lastname);
        cv.put("gender", gender);
        sdb.insert(Tablename, null, cv);
    }

    public ArrayList<model> Displaydata() {
        ArrayList<model> data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {Firstname, Lastname, Gender};
        Cursor cursor = db.query(Tablename, columns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String fn = cursor.getString(cursor.getColumnIndex(Firstname));
            String ln = cursor.getString(cursor.getColumnIndex(Lastname));
            String gender = cursor.getString(cursor.getColumnIndex(Gender));
            data.add(new model(fn, ln, gender));
        }
        cursor.close();
        db.close();

        return data;
    }

    public void Deletedata(String firstname) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Tablename, "firstname = ?", new String[]{firstname});
        db.close();
    }

    public void UpdateData(String firstname, String lastname,String gender) {
        SQLiteDatabase sdb = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("firstname", firstname);
        cv.put("lastname", lastname);
        cv.put("gender", gender);
        sdb.update(Tablename, cv, "firstname = ?", new String[]{firstname});
    }
}