package com.example.myfpd.layoutClasses;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfpd.Components.ComponentEvents.ComponentEventOnDelete;
import com.example.myfpd.Components.ComponentProducts;
import com.example.myfpd.Database.Products.ProductClass;
import com.example.myfpd.MyLibrary.MyLibraryLayout;
import com.example.myfpd.MyLibrary.MyLibraryMessage;
import com.example.myfpd.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

public class LayoutProducts extends AppCompatActivity {
    private String getTag() {return "layoutClasses main"; }
    Button btnShowAddProduct, btnBack;
    LinearLayout listProducts;
    TextView textViewCategory, textViewExpirationDate;
    ArrayList<ProductClass> arrayListProducts = new ArrayList<ProductClass>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyLibraryLayout.initLayoutPolicies(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.products);

        Objects.requireNonNull(getSupportActionBar()).hide();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        this.listProducts = (LinearLayout) findViewById(R.id.listCategories);
        this.btnBack = (Button) findViewById(R.id.btnBack);
        this.btnShowAddProduct = (Button) findViewById(R.id.btnShowAddCategory);
        this.textViewCategory = (TextView) findViewById(R.id.textViewCategoryTitle);
        this.textViewExpirationDate = (TextView) findViewById(R.id.textViewExpirationDate);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLibraryLayout.ChangeLayout(LayoutProducts.this, LayoutHome.class);
            }
        });

        btnShowAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLibraryLayout.ChangeLayout(LayoutProducts.this, LayoutAddProduct.class);
            }
        });

        this.textViewCategory.setOnClickListener(new View.OnClickListener() {
            class ProductComparator implements Comparator<ProductClass> {
                public int compare(ProductClass p1, ProductClass p2) {
                    return p1.categoryName.compareTo(p2.categoryName);
                }
            }

            class ProductComparatorReverse implements Comparator<ProductClass> {
                public int compare(ProductClass p1, ProductClass p2) {
                    return p2.categoryName.compareTo(p1.categoryName);
                }
            }

            @Override
            public void onClick(View view) {
                textViewCategory.setTag(textViewCategory.getTag().toString().equals("desc") ? "asc" : "desc");
                Collections.sort(arrayListProducts, textViewCategory.getTag().toString().equals("asc") ? new ProductComparator() : new ProductComparatorReverse());
                setItems();
            }
        });

        this.textViewExpirationDate.setOnClickListener(new View.OnClickListener() {
            class ProductComparator implements Comparator<ProductClass> {
                public int compare(ProductClass p1, ProductClass p2) {
                    return p1.expirationDate.compareTo(p2.expirationDate);
                }
            }

            class ProductComparatorReverse implements Comparator<ProductClass> {
                public int compare(ProductClass p1, ProductClass p2) {
                    return p2.expirationDate.compareTo(p1.expirationDate);
                }
            }

            @Override
            public void onClick(View view) {
                textViewExpirationDate.setTag(textViewExpirationDate.getTag().toString().equals("desc") ? "asc" : "desc");
                Collections.sort(arrayListProducts, textViewExpirationDate.getTag().toString().equals("asc") ? new ProductComparator() : new ProductComparatorReverse());
                setItems();
            }
        });

        this.getItems();
        this.setItems();
    }

    private void setItems(){
        this.listProducts.removeAllViews();
        for (ProductClass item : this.arrayListProducts) {
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
                                            for (int i = 0; i < arrayListProducts.size(); i++) {
                                                ProductClass findItem = arrayListProducts.get(i);
                                                if (findItem.id == data.id) {
                                                    arrayListProducts.remove(i);
                                                    setItems();
                                                    message.GetAlertDialog(LayoutProducts.this,
                                                            getString(R.string.deleted),
                                                            getString(R.string.itemDeleted),
                                                            false,
                                                            true,
                                                            getString(R.string.okay),
                                                            new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {

                                                                }
                                                            },
                                                            false,
                                                            null,
                                                            null
                                                    );
                                                    break;
                                                }
                                            }
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
        this.arrayListProducts.add(new ProductClass(1, "123Qwe123", new Date(), "Coca Cola", 1));
        this.arrayListProducts.add(new ProductClass(2, "234234", new Date(), "Fanta", 2));
        this.arrayListProducts.add(new ProductClass(3, "1ewrwer", new Date(), "Sprite", 3));
        this.arrayListProducts.add(new ProductClass(4, "345345wer", new Date("06/01/2021"), "Le Cola", 4));
        this.arrayListProducts.add(new ProductClass(5, "1ewrwer", new Date(), "Sprite", 3));
        this.arrayListProducts.add(new ProductClass(6, "345345wer", new Date(), "Le Cola", 4));
        this.arrayListProducts.add(new ProductClass(7, "1ewrwer", new Date(), "Sprite", 3));
        this.arrayListProducts.add(new ProductClass(8, "345345wer", new Date(), "Le Cola", 4));
        this.arrayListProducts.add(new ProductClass(9, "1ewrwer", new Date(), "Sprite", 3));
        this.arrayListProducts.add(new ProductClass(10, "345345wer", new Date(), "Le Cola", 4));
        this.arrayListProducts.add(new ProductClass(11, "1ewrwer", new Date(), "Sprite", 3));
        this.arrayListProducts.add(new ProductClass(12, "345345wer", new Date(), "Le Cola", 4));
    }
}