package com.example.myfpd.layoutClasses;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfpd.Database.DatabaseInit;
import com.example.myfpd.Database.Tables.Categories.CategoryClass;
import com.example.myfpd.MyLibrary.MyLibraryLayout;
import com.example.myfpd.MyLibrary.MyLibraryMessage;
import com.example.myfpd.MyLibrary.MyLibraryVariable;
import com.example.myfpd.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class LayoutAddProduct extends AppCompatActivity {
    private String getTag() {
        return "layoutClasses main";
    }


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

                if (
                        MyLibraryVariable.ClearVar(inputStockCode.getText(), "normal").toString().length() == 0 ||
                                arrayListCategories.isEmpty() ||
                                !MyLibraryVariable.isValidDateFormat(inputExpirationDate.getText().toString())
                ) {
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
                } else {
                    new AddProductAsync().execute(
                            inputStockCode.getText().toString(),
                            inputExpirationDate.getText().toString(),
                            inputCategory.getSelectedView().getTag().toString()
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

        new InitPageAsync().execute();
    }

    private void setCategories() {
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

    private void getCategories() {
        DatabaseInit db = new DatabaseInit(LayoutAddProduct.this);
        Cursor resData = db.getTableCategories().onSelect();
        while (resData.moveToNext()) {
            arrayListCategories.add(new CategoryClass(
                            resData.getLong(0),
                            resData.getString(1)
                    )
            );
        }
    }


    private class InitPageAsync extends AsyncTask<String, Void, Void> {

        ProgressDialog progressDialog = new ProgressDialog(new ContextThemeWrapper(LayoutAddProduct.this, R.style.Theme_AppCompat_DayNight_Dialog));

        @Override
        protected void onPreExecute() {
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.waiting));
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... values) {
            try {
                getCategories();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setCategories();
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

    boolean isItemAdded = false;
    private class AddProductAsync extends AsyncTask<String, Void, Void> {

        ProgressDialog progressDialog = new ProgressDialog(new ContextThemeWrapper(LayoutAddProduct.this, R.style.Theme_AppCompat_DayNight_Dialog));

        @Override
        protected void onPreExecute() {
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.waiting));
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... values) {
            try {
                DatabaseInit db = new DatabaseInit(LayoutAddProduct.this);

                Cursor resultCheck = db.getTableProducts().onSelectWithStockCode(values[0]);
                if(resultCheck.getCount() == 0){
                    db.getTableProducts().onInsert(
                            values[0],
                            values[1],
                            Long.parseLong(values[2])
                    );

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            inputStockCode.setText("");
                            inputExpirationDate.setText(MyLibraryVariable.convertDateFormat(new Date(), null));
                            inputCategory.setSelection(0);
                        }
                    });

                    isItemAdded = true;
                }else {
                    isItemAdded = false;
                }
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
            if(isItemAdded) {
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
            }else {
                message.GetAlertDialog(LayoutAddProduct.this,
                        getString(R.string.error),
                        getString(R.string.itemAvailable),
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
}