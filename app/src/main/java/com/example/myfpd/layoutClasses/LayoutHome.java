package com.example.myfpd.layoutClasses;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.myfpd.MyLibrary.MyLibraryLayout;
import com.example.myfpd.MyLibrary.MyLibraryMessage;
import com.example.myfpd.R;

import java.util.Objects;

public class LayoutHome extends AppCompatActivity {
    private String getTag() {return "layoutClasses main"; }
    Button btnProducts, btnCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Objects.requireNonNull(getSupportActionBar()).hide();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        this.btnProducts = (Button) findViewById(R.id.btnProducts);
        this.btnCategories = (Button) findViewById(R.id.btnCategories);

        this.btnProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyLibraryLayout.ChangeLayout(LayoutHome.this, LayoutProducts.class);
            }
        });
    }

    @Override
    public void onBackPressed() {
        try{
            showExitMessage();
        }catch(Exception exception){ Log.e(getTag(), "Error onBackPressed: " + exception.toString()); }
    }

    private void showExitMessage(){
        MyLibraryMessage message = new MyLibraryMessage();
        message.GetAlertDialog(this,
                this.getString(R.string.exitMessageTitle),
                this.getString(R.string.exitMessage),
                false,
                true,
                this.getString(R.string.exit),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                },
                true,
                this.getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel Button
                    }
                });
    }
}