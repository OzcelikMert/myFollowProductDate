package com.example.myfpd.Components;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myfpd.Components.ComponentEvents.ComponentEventOnDelete;
import com.example.myfpd.MyLibrary.MyLibraryLayout;
import com.example.myfpd.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ComponentProducts {
    private Context context;
    public long id;
    public String stockCode;
    public Date expirationDate;
    public String categoryName;
    public long categoryId;

    public ComponentProducts(
            Context context,
            LinearLayout linearLayout,
            long id,
            String stockCode,
            Date expirationDate,
            String categoryName,
            long categoryId,
            ComponentEventOnDelete<ComponentProducts> onDelete
    ) {
        this.context = context;
        this.id = id;
        this.stockCode = stockCode;
        this.expirationDate = expirationDate;
        this.categoryName = categoryName;
        this.categoryId = categoryId;

        LinearLayout elementLinearLayoutMain = this.elementLinearLayoutMain();
        elementLinearLayoutMain.addView(this.elementStockCode());
        elementLinearLayoutMain.addView(this.elementExpirationDate());
        elementLinearLayoutMain.addView(this.elementCategoryName());
        elementLinearLayoutMain.addView(this.elementDelete(onDelete));

        linearLayout.addView(elementLinearLayoutMain);
    }

    private LinearLayout elementLinearLayoutMain() {
        LinearLayout linearLayoutMain = new LinearLayout(this.context);
        linearLayoutMain.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        linearLayoutMain.setPadding(MyLibraryLayout.convertPixelToDP(this.context, 10), MyLibraryLayout.convertPixelToDP(this.context, 10), MyLibraryLayout.convertPixelToDP(this.context, 10), 0);
        return linearLayoutMain;
    }

    private TextView elementStockCode() {
        TextView textView = new TextView(this.context);
        textView.setText(this.stockCode);
        textView.setMinWidth(MyLibraryLayout.convertPixelToDP(this.context, 150));
        return textView;
    }

    private TextView elementExpirationDate() {
        TextView textView = new TextView(this.context);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        textView.setText(dateFormat.format(this.expirationDate));
        textView.setMinWidth(MyLibraryLayout.convertPixelToDP(this.context, 120));
        return textView;
    }

    private TextView elementCategoryName() {
        TextView textView = new TextView(this.context);
        textView.setText(this.categoryName);
        textView.setMinWidth(MyLibraryLayout.convertPixelToDP(this.context, 150));
        return textView;
    }

    private Button elementDelete(ComponentEventOnDelete<ComponentProducts> componentEvent) {
        Button button = new Button(this.context);
        button.setText(R.string.delete);
        button.setBackgroundColor(Color.rgb(223 ,48, 121));
        button.setMinWidth(MyLibraryLayout.convertPixelToDP(this.context, 75));
        //ComponentProducts _this = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                componentEvent.onDelete(ComponentProducts.this);
            }
        });
        return button;
    }
}
