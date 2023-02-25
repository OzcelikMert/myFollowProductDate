package com.example.myfpd.Database.Tables.Categories;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

public class TableCategories {
    public static final String tableName = "categories";
    public static final String columnId = "categoryId";
    public static final String columnTitle = "categoryTitle";
    public static final String columnCreatedAt = "categoryCreatedAt";
    SQLiteDatabase db;

    public TableCategories(SQLiteDatabase db) {
        this.db = db;
    }

    public void onCreate() {
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s TEXT, " +
                "%s TEXT" +
                ")", tableName, columnId, columnTitle, columnCreatedAt)
        );
    }

    public void onUpgrade() {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", tableName));
        onCreate();
    }

    public boolean onInsert(String title){
        ContentValues contentValues = new ContentValues();
        contentValues.put(columnTitle, title);
        contentValues.put(columnCreatedAt, String.valueOf(new Date().getTime()));
        long result = db.insert(tableName, null, contentValues);
        return result != -1;
    }

    public Cursor onSelect(){
        Cursor res = db.rawQuery(String.format("SELECT * FROM %s ORDER BY %s DESC", tableName, columnCreatedAt), null);
        return res;
    }

    public Integer onUpdate(String id, String title){
        ContentValues contentValues = new ContentValues();
        contentValues.put(columnTitle, title);
        return db.update(tableName, contentValues, String.format("%s = ?", columnId), new String[] {id});
    }

    public Integer onDelete(String id){
        return db.delete(tableName, String.format("%s = ?", columnId), new String[] {id});
    }
}
