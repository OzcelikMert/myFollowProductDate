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

public class ComponentCategories {
    private Context context;
    public long id;
    public String title;

    public ComponentCategories(
            Context context,
            LinearLayout linearLayout,
            long id,
            String title,
            ComponentEventOnDelete<ComponentCategories> onDelete
    ) {
        this.context = context;
        this.id = id;
        this.title = title;

        LinearLayout elementLinearLayoutMain = this.elementLinearLayoutMain();
        elementLinearLayoutMain.addView(this.elementTitle());
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

    private TextView elementTitle() {
        TextView textView = new TextView(this.context);
        textView.setText(this.title);
        textView.setMinWidth(MyLibraryLayout.convertPixelToDP(this.context, 250));
        return textView;
    }

    private Button elementDelete(ComponentEventOnDelete<ComponentCategories> componentEvent) {
        Button button = new Button(this.context);
        button.setText(R.string.delete);
        button.setBackgroundColor(Color.rgb(223 ,48, 121));
        button.setMinWidth(MyLibraryLayout.convertPixelToDP(this.context, 75));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                componentEvent.onDelete(ComponentCategories.this);
            }
        });
        return button;
    }
}
