package com.example.myfpd.Database.Tables.Products;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myfpd.Database.Tables.Categories.TableCategories;

import java.util.Date;

public class TableProducts {
    public static final String tableName = "products";
    public static final String columnId = "productId";
    public static final String columnStockCode = "productStockCode";
    public static final String columnExpirationDate = "productExpirationDate";
    public static final String columnCategoryId = "productCategoryId";
    public static final String columnCreatedAt = "productCreatedAt";
    SQLiteDatabase db;

    public TableProducts(SQLiteDatabase db) {
        this.db = db;
    }

    public void onCreate() {
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "%s INTEGER PRIMARY  KEY AUTOINCREMENT, " +
                "%s TEXT, " +
                "%s TEXT, " +
                "%s INTEGER, " +
                "%s TEXT" +
                ")", tableName, columnId, columnStockCode, columnExpirationDate, columnCategoryId, columnCreatedAt)
        );
    }

    public void onUpgrade() {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", tableName));
        onCreate();
    }

    public boolean onInsert(String stockCode, String expirationDate, long categoryId){
        ContentValues contentValues = new ContentValues();
        contentValues.put(columnStockCode, stockCode);
        contentValues.put(columnExpirationDate, expirationDate);
        contentValues.put(columnCategoryId, categoryId);
        contentValues.put(columnCreatedAt, String.valueOf(new Date().getTime()));
        long result = db.insert(tableName, null, contentValues);

        return result != -1;
    }

    public Cursor onSelect(){
        Cursor res = db.rawQuery(String.format(
                "SELECT * FROM %s LEFT JOIN %s ON %s=%s ORDER BY %s ASC, %s ASC",
                tableName,
                TableCategories.tableName,
                TableCategories.columnId,
                columnCategoryId,
                TableCategories.columnTitle,
                columnExpirationDate
        ), null);
        return res;
    }

    public Cursor onSelectWithStockCode(String stockCode){
        Cursor res = db.rawQuery(String.format(
                "SELECT * FROM %s where %s = ?",
                tableName,
                columnStockCode
        ), new String[] {stockCode});
        return res;
    }

    public Integer onUpdate(String id, String stockCode, String expirationDate, long categoryId){
        ContentValues contentValues = new ContentValues();
        contentValues.put(columnStockCode, stockCode);
        contentValues.put(columnExpirationDate, expirationDate);
        contentValues.put(columnCategoryId, categoryId);
        return db.update(tableName, contentValues, String.format("%s = ?", columnId), new String[] {id});
    }

    public Integer onDelete(String id, String categoryId){
        return db.delete(
                tableName,
                String.format("%s = ?", categoryId == null ? columnId : columnCategoryId),
                new String[] {categoryId == null ? id : categoryId});
    }
}
