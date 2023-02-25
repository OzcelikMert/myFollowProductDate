package com.example.myfpd.layoutClasses;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfpd.Components.ComponentEvents.ComponentEventOnDelete;
import com.example.myfpd.Components.ComponentEvents.ComponentPaginationEventOnClick;
import com.example.myfpd.Components.ComponentPagination;
import com.example.myfpd.Components.ComponentProducts;
import com.example.myfpd.Database.DatabaseInit;
import com.example.myfpd.Database.Tables.Products.ProductClass;
import com.example.myfpd.MyLibrary.MyLibraryLayout;
import com.example.myfpd.MyLibrary.MyLibraryMessage;
import com.example.myfpd.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

public class LayoutProducts extends AppCompatActivity {
    private String getTag() {
        return "layoutClasses main";
    }

    Button btnShowAddProduct, btnBack;
    LinearLayout listProducts, listPaginationProducts;
    TextView textViewCategory, textViewExpirationDate;
    ScrollView scrollViewProductsPage;
    ArrayList<ProductClass> arrayListProducts = new ArrayList<ProductClass>();
    int currentPage = 1;
    int itemShowCount = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyLibraryLayout.initLayoutPolicies(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.products);

        Objects.requireNonNull(getSupportActionBar()).hide();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        this.listProducts = (LinearLayout) findViewById(R.id.listCategories);
        this.listPaginationProducts = (LinearLayout) findViewById(R.id.listPaginationProducts);
        this.btnBack = (Button) findViewById(R.id.btnBack);
        this.btnShowAddProduct = (Button) findViewById(R.id.btnShowAddCategory);
        this.textViewCategory = (TextView) findViewById(R.id.textViewCategoryTitle);
        this.textViewExpirationDate = (TextView) findViewById(R.id.textViewExpirationDate);
        this.scrollViewProductsPage = (ScrollView) findViewById(R.id.scrollViewProductsPage);

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

        new InitPageAsync().execute();
    }

    private void setItems() {
        this.listProducts.removeAllViews();
        int startIndex = (currentPage - 1) * itemShowCount;
        int toIndex = startIndex + itemShowCount;
        toIndex = Math.min(arrayListProducts.size(), toIndex);
        ArrayList<ProductClass> subList = new ArrayList<ProductClass>(arrayListProducts.subList(startIndex, toIndex));
        for (ProductClass item : subList) {
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
                                            new DeleteProductAsync().execute(item.id);
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
        DatabaseInit db = new DatabaseInit(LayoutProducts.this);
        Cursor resData = db.getTableProducts().onSelect();
        while (resData.moveToNext()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            dateFormat.setLenient(false);

            try {
                arrayListProducts.add(new ProductClass(
                                resData.getLong(0),
                                resData.getString(1),
                                dateFormat.parse(resData.getString(2)),
                                resData.getString(6),
                                resData.getLong(5)
                        )
                );
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void setPaginationItems() {
        int arrayCount = arrayListProducts.size() == 0 ? 1 : arrayListProducts.size();
        int maxPage = (int)Math.ceil((double) arrayCount / itemShowCount);
        listPaginationProducts.removeAllViews();

        new ComponentPagination(
                this,
                this.listPaginationProducts,
                maxPage,
                this.currentPage,
                new ComponentPaginationEventOnClick() {
                    @Override
                    public void onClick(int page) {
                        currentPage = page;
                        setItems();
                        setPaginationItems();
                        scrollViewProductsPage.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollViewProductsPage.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }
                }
        );
    }

    private class InitPageAsync extends AsyncTask<String, Void, Void> {

        ProgressDialog progressDialog = new ProgressDialog(new ContextThemeWrapper(LayoutProducts.this, R.style.Theme_AppCompat_DayNight_Dialog));

        @Override
        protected void onPreExecute() {
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.waiting));
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... values) {
            try {
                getItems();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setItems();
                        setPaginationItems();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private class DeleteProductAsync extends AsyncTask<Long, Void, Void> {

        ProgressDialog progressDialog = new ProgressDialog(new ContextThemeWrapper(LayoutProducts.this, R.style.Theme_AppCompat_DayNight_Dialog));

        @Override
        protected void onPreExecute() {
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.waiting));
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Long... values) {
            try {
                DatabaseInit db = new DatabaseInit(LayoutProducts.this);
                for (long value : values) {
                    for (int i = 0; i < arrayListProducts.size(); i++) {
                        ProductClass findItem = arrayListProducts.get(i);
                        if (findItem.id == value) {
                            db.getTableProducts().onDelete(String.valueOf(value), null);
                            arrayListProducts.remove(i);
                            break;
                        }
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setItems();
                        setPaginationItems();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            progressDialog = null;
            MyLibraryMessage message = new MyLibraryMessage();
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
        }
    }
}