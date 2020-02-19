package com.example.perfectday.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class HistoryDatabase extends SQLHelper {

    SQLHelper sqlHelper;

    public HistoryDatabase(@Nullable Context context) {
        super(context);
        sqlHelper = new SQLHelper(context);
    }

    public void addNewLocation(String cityName) {
        SQLiteDatabase sqLiteDatabase = sqlHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLHelper.COLUMN_LOCATION, cityName);
        sqLiteDatabase.insert(SQLHelper.TABLE_NAME, null, contentValues);
        sqlHelper.close();
    }

    public ArrayList<String> getListLocation() {
        ArrayList<String> listLocation = new ArrayList<>();
        String sql = "SELECT * FROM " + SQLHelper.TABLE_NAME;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            listLocation.add(cursor.getString(cursor.getColumnIndex(SQLHelper.COLUMN_LOCATION)));
            cursor.moveToNext();
        }
        return listLocation;
    }

    public void updateData(ArrayList<String> listData) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "DELETE FROM " + SQLHelper.TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        if (listData.size() > 0) {
            for (int i = 0; i < listData.size(); i++) {
                addNewLocation(listData.get(i));
            }
        }
        sqLiteDatabase.close();
        sqlHelper.close();
    }
}
