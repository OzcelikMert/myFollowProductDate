package com.example.myfpd.layoutClasses;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.myfpd.Components.ComponentEvents.ComponentEventOnDelete;
import com.example.myfpd.Components.ComponentProducts;
import com.example.myfpd.Database.Products.ProductClass;
import com.example.myfpd.MyLibrary.MyLibraryMessage;
import com.example.myfpd.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class LayoutProducts extends AppCompatActivity {
    private String getTag() {return "layoutClasses main"; }
    Button btnProducts, btnCategories;
    LinearLayout listProducts;
    ArrayList<ProductClass> arrayList = new ArrayList<ProductClass>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.products);

        Objects.requireNonNull(getSupportActionBar()).hide();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        this.listProducts = (LinearLayout) findViewById(R.id.listProducts);
        /*this.btnProducts = (Button) findViewById(R.id.btnProducts);
        this.btnCategories = (Button) findViewById(R.id.btnCategories);

        this.btnProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayout(com.example.myfpd.Components.Products.class);
            }
        });*/

        this.setItems();
    }

    private void setItems(){
        this.getItems();
        for (ProductClass item : this.arrayList) {
            new ComponentProducts(
                    this,
                    this.listProducts,
                    item.id,
                    item.stockCode,
                    item.expirationDate,
                    item.categoryName,
                    item.categoryId,
                    new ComponentEventOnDelete<ComponentProducts>() {
                        @Override
                        public void onDelete(ComponentProducts data) {
                            MyLibraryMessage message = new MyLibraryMessage();
                            message.GetAlertDialog(LayoutProducts.this,
                                    String.format("%s - %s", data.stockCode, data.categoryName),
                                    getString(R.string.deleteMessage),
                                    false,
                                    true,
                                    getString(R.string.delete),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    },
                                    true,
                                    getString(R.string.cancel),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Cancel Button
                                        }
                                    });
                        }
                    }
            );
        }
    }

    private void getItems() {
        this.arrayList.add(new ProductClass(1, "123Qwe123", new Date(), "Coca Cola", 1));
        this.arrayList.add(new ProductClass(2, "234234", new Date(), "Fanta", 2));
        this.arrayList.add(new ProductClass(3, "1ewrwer", new Date(), "Sprite", 3));
        this.arrayList.add(new ProductClass(4, "345345wer", new Date(), "Le Cola", 4));
        this.arrayList.add(new ProductClass(3, "1ewrwer", new Date(), "Sprite", 3));
        this.arrayList.add(new ProductClass(4, "345345wer", new Date(), "Le Cola", 4));
        this.arrayList.add(new ProductClass(3, "1ewrwer", new Date(), "Sprite", 3));
        this.arrayList.add(new ProductClass(4, "345345wer", new Date(), "Le Cola", 4));
        this.arrayList.add(new ProductClass(3, "1ewrwer", new Date(), "Sprite", 3));
        this.arrayList.add(new ProductClass(4, "345345wer", new Date(), "Le Cola", 4));
        this.arrayList.add(new ProductClass(3, "1ewrwer", new Date(), "Sprite", 3));
        this.arrayList.add(new ProductClass(4, "345345wer", new Date(), "Le Cola", 4));
    }

    private void changeLayout(Class layout) {
        Intent intent = new Intent(LayoutProducts.this, layout);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}