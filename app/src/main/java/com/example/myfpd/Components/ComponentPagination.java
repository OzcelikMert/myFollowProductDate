package com.example.myfpd.Components;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.myfpd.Components.ComponentEvents.ComponentPaginationEventOnClick;
import com.example.myfpd.MyLibrary.MyLibraryLayout;

public class ComponentPagination {
    private Context context;
    public int maxPage;
    public int currentPage;

    public ComponentPagination(
            Context context,
            LinearLayout linearLayout,
            int maxPage,
            int currentPage,
            ComponentPaginationEventOnClick onClick
    ) {
        this.context = context;
        this.maxPage = maxPage;
        this.currentPage = currentPage;

        linearLayout.addView(this.elementPaginationButton("<-", 1, currentPage == 1, onClick));

        if(currentPage - 2 > 0){
            linearLayout.addView(this.elementPaginationButton(String.valueOf(currentPage - 2), currentPage - 2, false, onClick));
        }

        if(currentPage - 1 > 0){
            linearLayout.addView(this.elementPaginationButton(String.valueOf(currentPage - 1), currentPage - 1, false, onClick));
        }

        linearLayout.addView(this.elementPaginationButton(String.valueOf(currentPage), currentPage, true, onClick));

        if(currentPage + 1 <= maxPage){
            linearLayout.addView(this.elementPaginationButton(String.valueOf(currentPage + 1), currentPage + 1, false, onClick));
        }

        if(currentPage + 2 <= maxPage){
            linearLayout.addView(this.elementPaginationButton(String.valueOf(currentPage + 2), currentPage + 2, false, onClick));
        }

        linearLayout.addView(this.elementPaginationButton("->", maxPage, currentPage == maxPage, onClick));
    }

    private Button elementPaginationButton(String text, int page, boolean isDisabled, ComponentPaginationEventOnClick componentEvent) {
        Button button = new Button(this.context);
        button.setText(text);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                MyLibraryLayout.convertPixelToDP(this.context, 40),
                MyLibraryLayout.convertPixelToDP(this.context, 40)
        );
        params.setMargins(
                MyLibraryLayout.convertPixelToDP(this.context, 2),
                MyLibraryLayout.convertPixelToDP(this.context, 0),
                MyLibraryLayout.convertPixelToDP(this.context, 2),
                MyLibraryLayout.convertPixelToDP(this.context, 0)
        );
        params.gravity = Gravity.CENTER;
        button.setLayoutParams(params);
        button.setBackgroundColor(Color.rgb(98 ,0, 238));
        if(!isDisabled){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    componentEvent.onClick(page);
                }
            });
        }else {
            button.setBackgroundColor(Color.rgb(35 ,20, 20));
        }
        return button;
    }
}
