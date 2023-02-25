package com.example.myfpd.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myfpd.Database.Tables.Categories.TableCategories;
import com.example.myfpd.Database.Tables.Products.TableProducts;

public class DatabaseInit extends SQLiteOpenHelper {
    public DatabaseInit(Context context) {
        super(context, DatabaseValues.databaseName, null, DatabaseValues.databaseVersion);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TableProducts tableProducts = new TableProducts(db);
        tableProducts.onCreate();

        TableCategories tableCategories = new TableCategories(db);
        tableCategories.onCreate();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TableProducts tableProducts = new TableProducts(db);
        tableProducts.onUpgrade();
        tableProducts.onCreate();

        TableCategories tableCategories = new TableCategories(db);
        tableCategories.onUpgrade();
        tableCategories.onCreate();
    }

    public TableProducts getTableProducts() {
        return new TableProducts(this.getWritableDatabase());
    }

    public TableCategories getTableCategories() {
        return new TableCategories(this.getWritableDatabase());
    }
}
