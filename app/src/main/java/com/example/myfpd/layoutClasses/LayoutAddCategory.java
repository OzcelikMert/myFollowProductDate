package com.example.myfpd.layoutClasses;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

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
                    inputCategoryTitle.setText("");

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
        });

        this.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyLibraryLayout.ChangeLayout(LayoutAddCategory.this, LayoutCategories.class);
            }
        });
    }
}