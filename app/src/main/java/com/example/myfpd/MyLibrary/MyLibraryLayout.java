package com.example.myfpd.MyLibrary;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.util.TypedValue;

import androidx.appcompat.app.AppCompatDelegate;

public class MyLibraryLayout {
    /* Change Layout */
    public static void ChangeLayout(Context context, Class aClass){
        Intent intent = new Intent(context, aClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
    /* end Change Layout */
    public static int convertPixelToDP(Context context, int pixel) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixel, context.getResources().getDisplayMetrics());
    }
    public static void initLayoutPolicies(Context context) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}
