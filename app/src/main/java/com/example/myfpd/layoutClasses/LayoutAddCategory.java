package com.example.myfpd.layoutClasses;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfpd.Database.DatabaseInit;
import com.example.myfpd.MyLibrary.MyLibraryLayout;
import com.example.myfpd.MyLibrary.MyLibraryMessage;
import com.example.myfpd.MyLibrary.MyLibraryVariable;
import com.example.myfpd.R;

import java.util.Objects;

public class LayoutAddCategory extends AppCompatActivity {
    private String getTag() {return "layoutClasses main"; }


    Button btnAddCategory, btnCancel;
    EditText inputCategoryTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyLibraryLayout.initLayoutPolicies(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_category);

        Objects.requireNonNull(getSupportActionBar()).hide();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        this.inputCategoryTitle = (EditText) findViewById(R.id.inputCategoryTitle);
        this.btnAddCategory = (Button) findViewById(R.id.btnAddCategory);
        this.btnCancel = (Button) findViewById(R.id.btnCancel);

        this.btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyLibraryMessage message = new MyLibraryMessage();
                if(
                        MyLibraryVariable.ClearVar(inputCategoryTitle.getText(), "normal").toString().length() == 0
                ){
                    message.GetAlertDialog(LayoutAddCategory.this,
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
                    new AddCategoryAsync().execute(inputCategoryTitle.getText().toString());
                }
            }
        });

        this.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyLibraryLayout.ChangeLayout(LayoutAddCategory.this, LayoutCategories.class);
            }
        });

        new InitPageAsync().execute();
    }

    private class InitPageAsync extends AsyncTask<String, Void, Void> {

        ProgressDialog progressDialog = new ProgressDialog(new ContextThemeWrapper(LayoutAddCategory.this, R.style.Theme_AppCompat_DayNight_Dialog));

        @Override
        protected void onPreExecute() {
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.waiting));
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... values) {
            try {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

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

    private class AddCategoryAsync extends AsyncTask<String, Void, Void> {

        ProgressDialog progressDialog = new ProgressDialog(new ContextThemeWrapper(LayoutAddCategory.this, R.style.Theme_AppCompat_DayNight_Dialog));

        @Override
        protected void onPreExecute() {
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.waiting));
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... values) {
            try {
                DatabaseInit db = new DatabaseInit(LayoutAddCategory.this);
                db.getTableCategories().onInsert(
                        values[0]
                );

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        inputCategoryTitle.setText("");
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
            message.GetAlertDialog(LayoutAddCategory.this,
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
}