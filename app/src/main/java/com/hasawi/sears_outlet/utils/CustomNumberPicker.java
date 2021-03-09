package com.hasawi.sears_outlet.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.hasawi.sears_outlet.R;

public class CustomNumberPicker extends android.widget.NumberPicker {

    Typeface type;

    public CustomNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, int index,
                        android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        type = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/avenir_bold.otf");
        updateView(child);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);

        type = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/avenir_bold.otf");
        updateView(child);
    }

    private void updateView(View view) {

        if (view instanceof EditText) {
            ((EditText) view).setTypeface(type);
            ((EditText) view).setTextSize(16);
            ((EditText) view).setTextColor(getResources().getColor(
                    R.color.bright_blue));
        }

    }

}