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

import com.example.myfpd.Components.ComponentCategories;
import com.example.myfpd.Components.ComponentEvents.ComponentEventOnDelete;
import com.example.myfpd.Components.ComponentEvents.ComponentPaginationEventOnClick;
import com.example.myfpd.Components.ComponentPagination;
import com.example.myfpd.Database.DatabaseInit;
import com.example.myfpd.Database.Tables.Categories.CategoryClass;
import com.example.myfpd.MyLibrary.MyLibraryLayout;
import com.example.myfpd.MyLibrary.MyLibraryMessage;
import com.example.myfpd.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class LayoutCategories extends AppCompatActivity {
    private String getTag() {return "layoutClasses main"; }
    Button btnShowAddCategory, btnBack;
    LinearLayout listCategories, listPaginationCategories;
    TextView textViewCategoryTitle;
    ScrollView scrollViewCategoriesPage;
    ArrayList<CategoryClass> arrayListCategories = new ArrayList<CategoryClass>();
    int currentPage = 1;
    int itemShowCount = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyLibraryLayout.initLayoutPolicies(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories);

        Objects.requireNonNull(getSupportActionBar()).hide();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        this.listCategories = (LinearLayout) findViewById(R.id.listCategories);
        this.listPaginationCategories = (LinearLayout) findViewById(R.id.listPaginationCategories);
        this.btnBack = (Button) findViewById(R.id.btnBack);
        this.btnShowAddCategory = (Button) findViewById(R.id.btnShowAddCategory);
        this.textViewCategoryTitle = (TextView) findViewById(R.id.textViewCategoryTitle);
        this.scrollViewCategoriesPage = (ScrollView) findViewById(R.id.scrollViewCategoriesPage);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLibraryLayout.ChangeLayout(LayoutCategories.this, LayoutHome.class);
            }
        });

        btnShowAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLibraryLayout.ChangeLayout(LayoutCategories.this, LayoutAddCategory.class);
            }
        });

        this.textViewCategoryTitle.setOnClickListener(new View.OnClickListener() {
            class CategoryComparator implements Comparator<CategoryClass> {
                public int compare(CategoryClass p1, CategoryClass p2) {
                    return p1.title.compareTo(p2.title);
                }
            }

            class CategoryComparatorReverse implements Comparator<CategoryClass> {
                public int compare(CategoryClass p1, CategoryClass p2) {
                    return p2.title.compareTo(p1.title);
                }
            }

            @Override
            public void onClick(View view) {
                textViewCategoryTitle.setTag(textViewCategoryTitle.getTag().toString().equals("desc") ? "asc" : "desc");
                Collections.sort(arrayListCategories, textViewCategoryTitle.getTag().toString().equals("asc") ? new CategoryComparator() : new CategoryComparatorReverse());
                setItems();
            }
        });

        new InitPageAsync().execute();
    }

    private void setItems(){
        this.listCategories.removeAllViews();
        int startIndex = (currentPage - 1) * itemShowCount;
        int toIndex = startIndex + itemShowCount;
        toIndex = Math.min(arrayListCategories.size(), toIndex);
        ArrayList<CategoryClass> subList = new ArrayList<CategoryClass>(arrayListCategories.subList(startIndex, toIndex));
        for (CategoryClass item : subList) {
            new ComponentCategories(
                    this,
                    this.listCategories,
                    item.id,
                    item.title,
                    new ComponentEventOnDelete<ComponentCategories>() {
                        @Override
                        public void onDelete(ComponentCategories data) {
                            MyLibraryMessage message = new MyLibraryMessage();
                            message.GetAlertDialog(LayoutCategories.this,
                                    String.format("%s", data.title),
                                    getString(R.string.deleteMessage),
                                    false,
                                    true,
                                    getString(R.string.delete),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            new DeleteCategoryAsync().execute(item.id);
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
        DatabaseInit db = new DatabaseInit(LayoutCategories.this);
        Cursor resData = db.getTableCategories().onSelect();
        while (resData.moveToNext()) {
            arrayListCategories.add(new CategoryClass(
                            resData.getLong(0),
                            resData.getString(1)
                    )
            );
        }
    }

    private void setPaginationItems() {
        int arrayCount = arrayListCategories.size() == 0 ? 1 : arrayListCategories.size();
        int maxPage = (int)Math.ceil((double) arrayCount / itemShowCount);
        listPaginationCategories.removeAllViews();

        new ComponentPagination(
                this,
                this.listPaginationCategories,
                maxPage,
                this.currentPage,
                new ComponentPaginationEventOnClick() {
                    @Override
                    public void onClick(int page) {
                        currentPage = page;
                        setItems();
                        setPaginationItems();
                        scrollViewCategoriesPage.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollViewCategoriesPage.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }
                }
        );
    }

    private class InitPageAsync extends AsyncTask<String, Void, Void> {

        ProgressDialog progressDialog = new ProgressDialog(new ContextThemeWrapper(LayoutCategories.this, R.style.Theme_AppCompat_DayNight_Dialog));

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

    private class DeleteCategoryAsync extends AsyncTask<Long, Void, Void> {

        ProgressDialog progressDialog = new ProgressDialog(new ContextThemeWrapper(LayoutCategories.this, R.style.Theme_AppCompat_DayNight_Dialog));

        @Override
        protected void onPreExecute() {
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.waiting));
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Long... values) {
            try {
                DatabaseInit db = new DatabaseInit(LayoutCategories.this);
                for (long value : values) {
                    for (int i = 0; i < arrayListCategories.size(); i++) {
                        CategoryClass findItem = arrayListCategories.get(i);
                        if (findItem.id == value) {
                            db.getTableCategories().onDelete(String.valueOf(value));
                            db.getTableProducts().onDelete(null, String.valueOf(value));
                            arrayListCategories.remove(i);
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
            message.GetAlertDialog(LayoutCategories.this,
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