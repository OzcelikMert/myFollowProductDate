package com.example.myfpd.layoutClasses;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfpd.Components.ComponentCategories;
import com.example.myfpd.Components.ComponentEvents.ComponentEventOnDelete;
import com.example.myfpd.Database.Categories.CategoryClass;
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
    LinearLayout listCategories;
    TextView textViewCategoryTitle;
    ArrayList<CategoryClass> arrayListCategories = new ArrayList<CategoryClass>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyLibraryLayout.initLayoutPolicies(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories);

        Objects.requireNonNull(getSupportActionBar()).hide();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        this.listCategories = (LinearLayout) findViewById(R.id.listCategories);
        this.btnBack = (Button) findViewById(R.id.btnBack);
        this.btnShowAddCategory = (Button) findViewById(R.id.btnShowAddCategory);
        this.textViewCategoryTitle = (TextView) findViewById(R.id.textViewCategoryTitle);

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

        this.getItems();
        this.setItems();
    }

    private void setItems(){
        this.listCategories.removeAllViews();
        for (CategoryClass item : this.arrayListCategories) {
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
                                            for (int i = 0; i < arrayListCategories.size(); i++) {
                                                CategoryClass findItem = arrayListCategories.get(i);
                                                if (findItem.id == data.id) {
                                                    arrayListCategories.remove(i);
                                                    setItems();
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
        this.arrayListCategories.add(new CategoryClass(1, "Fanta"));
        this.arrayListCategories.add(new CategoryClass(2, "Coca Cola"));
        this.arrayListCategories.add(new CategoryClass(3, "Sprite"));
        this.arrayListCategories.add(new CategoryClass(4, "Ice Tea"));
        this.arrayListCategories.add(new CategoryClass(5, "Doritos"));
        this.arrayListCategories.add(new CategoryClass(6, "Patso"));
        this.arrayListCategories.add(new CategoryClass(7, "Lays"));
    }
}