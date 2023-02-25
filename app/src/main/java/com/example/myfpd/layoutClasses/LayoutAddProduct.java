package com.example.myfpd.layoutClasses;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfpd.Database.Categories.CategoryClass;
import com.example.myfpd.MyLibrary.MyLibraryLayout;
import com.example.myfpd.MyLibrary.MyLibraryMessage;
import com.example.myfpd.MyLibrary.MyLibraryVariable;
import com.example.myfpd.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class LayoutAddProduct extends AppCompatActivity {
    private String getTag() {return "layoutClasses main"; }


    Button btnAddProduct, btnCancel;
    EditText inputStockCode, inputExpirationDate;
    Spinner inputCategory;
    ArrayList<CategoryClass> arrayListCategories = new ArrayList<CategoryClass>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyLibraryLayout.initLayoutPolicies(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);

        Objects.requireNonNull(getSupportActionBar()).hide();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        this.inputStockCode = (EditText) findViewById(R.id.inputCategoryTitle);
        this.inputExpirationDate = (EditText) findViewById(R.id.inputExpirationDate);
        this.inputCategory = (Spinner) findViewById(R.id.inputCategory);
        this.btnAddProduct = (Button) findViewById(R.id.btnAddCategory);
        this.btnCancel = (Button) findViewById(R.id.btnCancel);

        this.inputExpirationDate.setText(MyLibraryVariable.convertDateFormat(new Date(), null));

        this.btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyLibraryMessage message = new MyLibraryMessage();

                if(
                        MyLibraryVariable.ClearVar(inputStockCode.getText(), "normal").toString().length() == 0 ||
                        arrayListCategories.isEmpty() ||
                        !MyLibraryVariable.isValidDateFormat(inputExpirationDate.getText().toString())
                ){
                    message.GetAlertDialog(LayoutAddProduct.this,
                            getString(R.string.error),
                            getString(R.string.wrongOrEmptyError),
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
                }else {
                    inputStockCode.setText("");
                    inputExpirationDate.setText(MyLibraryVariable.convertDateFormat(new Date(), null));
                    inputCategory.setSelection(0);

                    message.GetAlertDialog(LayoutAddProduct.this,
                            getString(R.string.added),
                            getString(R.string.itemAdded),
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
        });

        this.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyLibraryLayout.ChangeLayout(LayoutAddProduct.this, LayoutProducts.class);
            }
        });

        this.getCategories();
    }

    private void getCategories() {
        this.arrayListCategories.add(new CategoryClass(1, "Coca Cola"));
        this.arrayListCategories.add(new CategoryClass(2, "Fanta"));
        this.arrayListCategories.add(new CategoryClass(3, "Sprite"));
        this.arrayListCategories.add(new CategoryClass(4, "Lipton"));
        this.arrayListCategories.add(new CategoryClass(3, "Doritos"));
        this.arrayListCategories.add(new CategoryClass(4, "Patso"));
        this.arrayListCategories.add(new CategoryClass(3, "Eti Cikolata Gofret (250g)"));


        ArrayAdapter<CategoryClass> adapter = new ArrayAdapter<CategoryClass>(this, android.R.layout.simple_spinner_dropdown_item, this.arrayListCategories) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setText(arrayListCategories.get(position).title);
                textView.setTag(arrayListCategories.get(position).id);
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setText(arrayListCategories.get(position).title);
                textView.setTag(arrayListCategories.get(position).id);
                return view;
            }
        };

        this.inputCategory.setAdapter(adapter);
    }
}